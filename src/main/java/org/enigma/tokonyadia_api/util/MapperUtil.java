package org.enigma.tokonyadia_api.util;

import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.dto.response.*;
import org.enigma.tokonyadia_api.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MapperUtil {
    public static PersonResponse toPersonResponse(Person person) {
        UserResponse userResponse = toUserResponse(person.getUserAccount());
        AuditInfoResponse auditInfoResponse = auditInfoResponse(person);
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .gender(person.getGender().getDescription())
                .address(person.getAddress())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getEmail())
                .userAccountId(userResponse.getId())
                .role(userResponse.getRole())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(product);
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .store(product.getStore().getName())
                .images(product.getProductImages() != null ? product.getProductImages().stream().map(MapperUtil::toProductImageResponse).toList() : new ArrayList<>())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static StoreResponse toStoreResponse(Store store) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(store);
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .personId(store.getPerson().getId())
                .siup(store.getSiup())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static ProductDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        return ProductDetailResponse.builder()
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProduct().getName())
                .price(orderDetail.getProduct().getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public static OrderResponse toOrderResponse(Order order) {
        Optional<List<OrderDetail>> optionalOrderDetailList = Optional.ofNullable(order.getOrderDetails());

        List<OrderDetail> orderDetails = optionalOrderDetailList
                .orElse(Collections.emptyList())
                .stream().toList();

        AuditInfoResponse auditInfoResponse = auditInfoResponse(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .personId(order.getPerson().getId())
                .personName(order.getPerson().getName())
                .personPhoneNumber(order.getPerson().getPhoneNumber())
                .orderDate(order.getOrderDate().toString())
                .orderStatus(String.valueOf(order.getOrderStatus()))
                .orderDetails(MapperUtil.toOrderListByStoreResponse(orderDetails))
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static List<OrderByStoreResponse> toOrderListByStoreResponse(List<OrderDetail> orderDetails) {
        List<OrderByStoreResponse> orderList = new ArrayList<>();
        List<ProductDetailResponse> productDetailResponse = MapperUtil.toProductDetailResponse(orderDetails);

        orderDetails.forEach(orderDetail -> orderList.add(
                OrderByStoreResponse.builder()
                        .orderDetailId(orderDetail.getId())
                        .storeId(orderDetail.getProduct().getStore().getId())
                        .storeName(orderDetail.getProduct().getStore().getName())
                        .products(productDetailResponse)
                        .shipment(orderDetail.getShipment() != null ? toShipmentResponse(orderDetail.getShipment()) : null)
                        .build()
        ));
        return orderList;
    }

    public static List<ProductDetailResponse> toProductDetailResponse(List<OrderDetail> orderDetails) {
        List<ProductDetailResponse> productDetailResponse = new ArrayList<>();
        orderDetails.forEach(orderDetail -> productDetailResponse.add(
                ProductDetailResponse.builder()
                        .productId(orderDetail.getProduct().getId())
                        .productName(orderDetail.getProduct().getName())
                        .price(orderDetail.getProduct().getPrice())
                        .quantity(orderDetail.getQuantity())
                        .build()
        ));
        return productDetailResponse;
    }

    public static UserResponse toUserResponse(UserAccount userAccount) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(userAccount);
        return UserResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(productCategory);
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static ShipmentResponse toShipmentResponse(Shipment shipment) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(shipment);
        return ShipmentResponse.builder()
                .shipmentId(shipment.getId())
                .orderDetailId(shipment.getOrderDetail().getId())
                .deliveryDate(shipment.getDeliveryDate().toString())
                .deliveryFrom(shipment.getDeliveryFrom())
                .deliveryTo(shipment.getDeliveryTo())
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static ProductRatingResponse toProductRatingResponse(ProductRating productRating) {
        return ProductRatingResponse.builder()
                .id(productRating.getId())
                .productId(productRating.getProduct().getId())
                .personId(productRating.getPerson().getId())
                .rating(productRating.getRating())
                .review(productRating.getReview())
                .build();
    }

    public static FileResponse toProductImageResponse(ProductImage productImage) {
        return FileResponse.builder()
                .id(productImage.getId())
                .url("/api/images/" + productImage.getId())
                .build();
    }

    public static InvoiceResponse toInvoiceResponse(Invoice invoice) {
        List<InvoiceItemResponse> invoiceItemResponseList = invoice.getInvoiceItems()
                .stream().map(MapperUtil::toInvoiceItemResponse).toList();
        AuditInfoResponse auditInfoResponse = auditInfoResponse(invoice);
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .orderId(invoice.getOrder().getId())
                .invoiceCode(invoice.getInvoiceCode())
                .customerName(invoice.getCustomerName())
                .totalAmount(invoice.getTotalAmount())
                .shopName(invoice.getShopName())
                .items(invoiceItemResponseList)
                .auditInfo(auditInfoResponse)
                .build();
    }

    public static InvoiceItemResponse toInvoiceItemResponse(InvoiceItem invoiceItem) {
        return InvoiceItemResponse.builder()
                .itemId(invoiceItem.getId())
                .productName(invoiceItem.getProductName())
                .quantity(invoiceItem.getQuantity())
                .price(invoiceItem.getProductPrice())
                .totalPrice(invoiceItem.getTotalPrice())
                .build();
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        AuditInfoResponse auditInfoResponse = auditInfoResponse(payment);
        return PaymentResponse.builder()
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .auditInfo(auditInfoResponse)
                .build();
    }

    private static <T extends Auditable<String>> AuditInfoResponse auditInfoResponse(T auditInfo) {
        return AuditInfoResponse.builder()
                .createdBy(auditInfo.getCreatedBy() != null ? auditInfo.getCreatedBy() : null)
                .createdDate(auditInfo.getCreatedDate() != null ? auditInfo.getCreatedDate().toString() : null)
                .updatedBy(auditInfo.getUpdatedBy() != null ? auditInfo.getUpdatedBy() : null)
                .updatedDate(auditInfo.getUpdatedDate() != null ? auditInfo.getUpdatedDate().toString() : null)
                .build();
    }
}
