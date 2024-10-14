package org.enigma.tokonyadia_api.utils;

import org.enigma.tokonyadia_api.dto.response.*;
import org.enigma.tokonyadia_api.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapperUtil {
    public static CustomerResponse toCustomerResponse(Customer customer) {
        UserResponse userResponse = toUserResponse(customer.getUserAccount());
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .userAccountId(userResponse.getId())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        Optional<Store> storeOpt = Optional.ofNullable(product.getStore());
        Optional<ProductPromo> productPromoOpt = Optional.ofNullable(product.getProductPromo());
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .storeId(storeOpt.map(Store::getId).orElse(null))
                .productPromoId(productPromoOpt.map(ProductPromo::getId).orElse(null))
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

    public static OrderDetailResponse toTransactionDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .productId(orderDetail.getId())
                .productName(orderDetail.getProduct().getName())
                .price(orderDetail.getProduct().getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public static List<OrderDetailResponse> toTransactionDetailListResponse(List<OrderDetail> orderDetail) {
        List<OrderDetailResponse> orderDetailRespons = new ArrayList<>();
        orderDetail.forEach(transactionDetailResponse -> orderDetailRespons.add(toTransactionDetailResponse(transactionDetailResponse)));
        return orderDetailRespons;
    }

    public static OrderResponse toTransactionResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomer().getName())
                .customerPhoneNumber(order.getCustomer().getPhoneNumber())
                .storeName(order.getOrderDetails().get(0).getProduct().getStore().getName())
                .transactionDate(order.getOrderDate())
                .transactionsDetails(toTransactionDetailListResponse(order.getOrderDetails()))
                .build();
    }

    public static List<OrderResponse> toTransactionListResponse(List<Order> orders) {
        List<OrderResponse> orderRespons = new ArrayList<>();
        orders.forEach(transactionResponse -> orderRespons.add(toTransactionResponse(transactionResponse)));
        return orderRespons;
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
}
