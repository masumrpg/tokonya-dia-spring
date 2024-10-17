package org.enigma.tokonyadia_api.util;

import org.enigma.tokonyadia_api.dto.response.*;
import org.enigma.tokonyadia_api.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MapperUtil {
    public static PersonResponse toPersonResponse(Person person) {
        UserResponse userResponse = toUserResponse(person.getUserAccount());
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .imageUrl(person.getImgUrl())
                .gender(person.getGender().getDescription())
                .address(person.getAddress())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getEmail())
                .userAccountId(userResponse.getId())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        Optional<Store> storeOpt = Optional.ofNullable(product.getStore());
        Optional<List<ProductPromo>> productPromoOpts = Optional.ofNullable(product.getProductPromo());
        List<ProductPromoResponse> promoResponses = productPromoOpts
                .orElse(Collections.emptyList())
                .stream()
                .map(MapperUtil::toProductPromoResponse)
                .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getImgUrl())
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .storeId(storeOpt.map(Store::getId).orElse(null))
                .productPromoId(promoResponses)
                .build();
    }

    public static StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .siup(store.getSiup())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
                .build();
    }

    public static OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .productId(orderDetail.getId())
                .productName(orderDetail.getProduct().getName())
                .storeName(orderDetail.getProduct().getStore().getName())
                .price(orderDetail.getProduct().getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public static List<OrderDetailResponse> toOrderDetailListResponse(List<OrderDetail> orderDetail) {
        List<OrderDetailResponse> orderDetailResponse = new ArrayList<>();
        orderDetail.forEach(orderDetail1 -> orderDetailResponse.add(toOrderDetailResponse(orderDetail1)));
        return orderDetailResponse;
    }

    public static OrderResponse toOrderResponse(Order order) {
        Optional<List<OrderDetail>> orderDetailsOps = Optional.ofNullable(order.getOrderDetails());

        List<OrderDetailResponse> orderDetailResponses = orderDetailsOps
                .orElse(Collections.emptyList())
                .stream()
                .map(MapperUtil::toOrderDetailResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .personName(order.getPerson().getName())
                .personPhoneNumber(order.getPerson().getPhoneNumber())
                .orderDate(order.getOrderDate().toString())
                .orderDetails(orderDetailResponses)
                .build();
    }

    public static List<OrderResponse> toOrderListResponse(List<Order> orders) {
        List<OrderResponse> orderResponse = new ArrayList<>();
        orders.forEach(order -> orderResponse.add(toOrderResponse(order)));
        return orderResponse;
    }

    public static UserResponse toUserResponse(UserAccount userAccount) {
        return UserResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .build();
    }

    public static ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .build();
    }

    public static ProductPromoResponse toProductPromoResponse(ProductPromo productPromo) {
        return ProductPromoResponse.builder()
                .id(productPromo.getId())
                .promoId(productPromo.getProduct().getId())
                .promoCode(productPromo.getPromoCode())
                .discount(productPromo.getDiscountPercentage())
                .startDateTime(productPromo.getStartDateTime().toString())
                .endDateTime(productPromo.getEndDateTime().toString())
                .build();
    }

    public static ShipmentResponse toShipmentResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .shipmentId(shipment.getId())
                .orderId(shipment.getOrder().getId())
                .courierName(shipment.getCourierName().getDescription())
                .receipt(shipment.getReceipt())
                .deliveryDate(shipment.getDeliveryDate().toString())
                .deliveryFrom(shipment.getDeliveryFrom())
                .deliveryTo(shipment.getDeliveryTo())
                .estimateDate(shipment.getEstimateDate().toString())
                .status(shipment.getStatus().toString())
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
}
