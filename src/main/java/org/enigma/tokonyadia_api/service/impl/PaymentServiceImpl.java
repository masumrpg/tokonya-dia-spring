package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.client.MidtransApiClient;
import org.enigma.tokonyadia_api.constant.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.client.MidtransAppClient;
import org.enigma.tokonyadia_api.constant.OrderStatus;
import org.enigma.tokonyadia_api.constant.PaymentStatus;
import org.enigma.tokonyadia_api.dto.request.*;
import org.enigma.tokonyadia_api.dto.response.MidtransNotificationRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import org.enigma.tokonyadia_api.dto.response.MidtransTransactionResponse;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;
import org.enigma.tokonyadia_api.entity.*;
import org.enigma.tokonyadia_api.repository.PaymentRepository;
import org.enigma.tokonyadia_api.service.InvoiceService;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.service.PaymentService;
import org.enigma.tokonyadia_api.util.HashUtil;
import org.enigma.tokonyadia_api.util.InvoiceCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final MidtransAppClient midtransAppClient;
    private final MidtransApiClient midtransApiClient;
    private final InvoiceService invoiceService;

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = validateOrderForPayment(request.getOrderId());
        long amount = calculateOrderAmount(order);
        MidtransSnapResponse snapTransaction = initiateMidtransPayment(order, amount);
        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .tokenSnap(snapTransaction.getToken())
                .redirectUrl(snapTransaction.getRedirectUrl())
                .build();
        paymentRepository.saveAndFlush(payment);

        orderService.updateOrderStatus(order.getId(), OrderStatus.PENDING);
        return mapToPaymentResponse(payment);
    }

    private long calculateOrderAmount(Order order) {
        return order.getOrderDetails().stream()
                .mapToLong(detail -> detail.getQuantity() * detail.getPrice())
                .sum();
    }

    private MidtransSnapResponse initiateMidtransPayment(Order order, long amount) {
        List<MidtransItemDetailRequest> itemDetails = order.getOrderDetails().stream().map(orderDetail -> MidtransItemDetailRequest.builder()
                .id(orderDetail.getProduct().getId())
                .name(orderDetail.getProduct().getName())
                .category(orderDetail.getProduct().getCategory().getName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .build()).toList();

        MidtransPersonDetailsRequest customerDetailsRequest = MidtransPersonDetailsRequest.builder()
                .firstName(order.getPerson().getName())
                .email(order.getPerson().getEmail())
                .phone(order.getPerson().getPhoneNumber())
                .build();

        MidtransPaymentRequest midtransPaymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(MidtransTransactionRequest.builder()
                        .orderId(order.getId())
                        .grossAmount(amount)
                        .build())
                .enabledPayments(List.of("bca_va", "gopay", "shopeepay", "other_qris"))
                .itemDetails(itemDetails)
                .customerDetails(customerDetailsRequest)
                .build();
        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        return midtransAppClient.createSnapTransaction(midtransPaymentRequest, headerValue);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getNotification(MidtransNotificationRequest request) {
        log.info("Start getNotification: {}", System.currentTimeMillis());
        validateSignatureKey(request.getOrderId(), request.getStatusCode(), request.getGrossAmount(), request.getSignatureKey());
        updatePaymentStatus(request.getOrderId(), request.getTransactionStatus());
        log.info("End getNotification: {}", System.currentTimeMillis());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse getPaymentStatusByOrderId(String orderId) {
        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        MidtransTransactionResponse transaction = midtransApiClient.getTransactionStatusByOrderId(orderId, headerValue);
        validateSignatureKey(transaction.getOrderId(), transaction.getStatusCode(), transaction.getGrossAmount(), transaction.getSignatureKey());
        Payment payment = updatePaymentStatus(transaction.getOrderId(), transaction.getTransactionStatus());

        boolean existByOrder = invoiceService.existByOrder(payment.getOrder());
        if (!existByOrder && payment.getPaymentStatus().equals(PaymentStatus.SETTLEMENT)) {
            Set<Store> uniqueStores = payment.getOrder().getOrderDetails().stream()
                    .map(orderDetail -> orderDetail.getProduct().getStore())
                    .collect(Collectors.toSet());

            List<Invoice> invoices = new ArrayList<>();

            for (Store store : uniqueStores) {
                List<InvoiceItem> invoiceItems = new ArrayList<>();
                Invoice invoice = Invoice.builder()
                        .order(payment.getOrder())
                        .invoiceCode(InvoiceCodeGenerator.generateInvoiceCode(store.getName(), payment.getId())) // Gunakan nama store dari store saat ini
                        .customerName(payment.getOrder().getPerson().getName())
                        .shopName(store.getName())
                        .build();

                payment.getOrder().getOrderDetails().stream()
                        .filter(orderDetail -> orderDetail.getProduct().getStore().equals(store))
                        .forEach(orderDetail -> {
                            InvoiceItem invoiceItem = InvoiceItem.builder()
                                    .invoice(invoice)
                                    .productName(orderDetail.getProduct().getName())
                                    .quantity(orderDetail.getQuantity())
                                    .productPrice(orderDetail.getPrice())
                                    .totalPrice((double) (orderDetail.getPrice() * orderDetail.getQuantity()))
                                    .build();
                            invoiceItems.add(invoiceItem);
                        });

                double storeTotalAmount = invoiceItems.stream()
                        .mapToDouble(InvoiceItem::getTotalPrice)
                        .sum();

                invoice.setInvoiceItems(invoiceItems);
                invoice.setTotalAmount(storeTotalAmount);

                invoices.add(invoice);
            }

            payment.getOrder().getInvoices().addAll(invoices);
        }

        return mapToPaymentResponse(payment);
    }

    private Payment getByOrderIdOrThrowNotFound(String orderId) {
        return paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment order not found"));
    }

    private Order validateOrderForPayment(String orderId) {
        Order order = orderService.getOne(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ORDER_IS_NOT_PENDING);
        }
        return order;
    }

    private Payment updatePaymentStatus(String orderId, String transactionStatus) {
        Payment payment = getByOrderIdOrThrowNotFound(orderId);
        PaymentStatus newPaymentStatus = PaymentStatus.findByDesc(transactionStatus);
        payment.setPaymentStatus(newPaymentStatus);

        if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.SETTLEMENT)) {
            payment.getOrder().setOrderStatus(OrderStatus.CONFIRMED);
        }

        paymentRepository.saveAndFlush(payment);
        return payment;
    }

    private void validateSignatureKey(String orderId, String statusCode, String grossAmount, String midtransSignatureKey) {
        String rawString = orderId + statusCode + grossAmount + MIDTRANS_SERVER_KEY;
        String signatureKey = HashUtil.encryptThisString(rawString);
        if (!signatureKey.equalsIgnoreCase(midtransSignatureKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature key");
        }
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .build();
    }
}
