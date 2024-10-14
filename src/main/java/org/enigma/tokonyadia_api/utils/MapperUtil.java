package org.enigma.tokonyadia_api.utils;

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
                .orderDate(order.getOrderDate())
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
                .discountPercentage(productPromo.getDiscountPercentage())
                .startDateTime(productPromo.getStartDateTime().toString())
                .endDateTime(productPromo.getEndDateTime().toString())
                .build();
    }
}
