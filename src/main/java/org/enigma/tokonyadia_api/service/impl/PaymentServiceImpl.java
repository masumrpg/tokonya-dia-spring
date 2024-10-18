package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.constant.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.client.MidtransClient;
import org.enigma.tokonyadia_api.constant.OrderStatus;
import org.enigma.tokonyadia_api.constant.PaymentStatus;
import org.enigma.tokonyadia_api.dto.request.MidtransPaymentRequest;
import org.enigma.tokonyadia_api.dto.request.MidtransTransactionRequest;
import org.enigma.tokonyadia_api.dto.request.PaymentRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransNotificationRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.entity.Payment;
import org.enigma.tokonyadia_api.repository.PaymentRepository;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.service.PaymentService;
import org.enigma.tokonyadia_api.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final MidtransClient midtransClient;

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderService.getOne(request.getOrderId());

        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ORDER_IS_NOT_PENDING);
        }

        long amount = 0;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Integer quantity = orderDetail.getQuantity();
            Long price = orderDetail.getPrice();
            amount += quantity * price;
        }

        MidtransPaymentRequest midtransPaymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(MidtransTransactionRequest.builder()
                        .orderId(order.getId())
                        .grossAmount(amount)
                        .build())
                .enabledPayments(List.of("bca_va", "gopay", "shopeepay", "other_qris"))
                .build();

        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        MidtransSnapResponse snapTransaction = midtransClient.createSnapTransaction(midtransPaymentRequest, headerValue);

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .tokenSnap(snapTransaction.getToken())
                .redirectUrl(snapTransaction.getRedirectUrl())
                .build();
        paymentRepository.saveAndFlush(payment);

        UpdateOrderStatusRequest statusRequest = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.PENDING).build();
        orderService.updateOrderStatus(order.getId(), statusRequest);

        return PaymentResponse.builder()
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getNotification(MidtransNotificationRequest request) {
        log.info("Start getNotification: {}", System.currentTimeMillis());
        if (!validateSignatureKey(request))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid signature key");
        Payment payment = paymentRepository.findByOrder_Id(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment order not found"));

        PaymentStatus newPaymentStatus = PaymentStatus.findByDesc(request.getTransactionStatus());
        payment.setPaymentStatus(newPaymentStatus);

        Order order = orderService.getOne(request.getOrderId());

        if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.SETTLEMENT)) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        UpdateOrderStatusRequest updateOrderStatusRequest = UpdateOrderStatusRequest.builder()
                .status(order.getOrderStatus())
                .build();
        orderService.updateOrderStatus(order.getId(), updateOrderStatusRequest);
        paymentRepository.saveAndFlush(payment);
        log.info("End getNotification: {}", System.currentTimeMillis());
    }


    private boolean validateSignatureKey(MidtransNotificationRequest request) {
        String rawString = request.getOrderId() + request.getStatusCode() + request.getGrossAmount() + MIDTRANS_SERVER_KEY;
        String signatureKey = HashUtil.encryptThisString(rawString);
        return request.getSignatureKey().equalsIgnoreCase(signatureKey);
    }
}
