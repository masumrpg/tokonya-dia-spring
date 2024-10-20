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
                .gender(person.getGender().getDescription())
                .address(person.getAddress())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getEmail())
                .userAccountId(userResponse.getId())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .storeId(product.getStore().getId())
                .images(product.getProductImages() != null ? product.getProductImages().stream().map(MapperUtil::toProductImageResponse).toList() : new ArrayList<>())
                .build();
    }

    public static StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .personId(store.getPerson().getId())
                .siup(store.getSiup())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
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

        return OrderResponse.builder()
                .orderId(order.getId())
                .personId(order.getPerson().getId())
                .personName(order.getPerson().getName())
                .personPhoneNumber(order.getPerson().getPhoneNumber())
                .orderDate(order.getOrderDate().toString())
                .orderStatus(String.valueOf(order.getOrderStatus()))
                .orderDetails(MapperUtil.toOrderListByStoreResponse(orderDetails))
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

    public static ShipmentResponse toShipmentResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .shipmentId(shipment.getId())
                .orderDetailId(shipment.getOrderDetail().getId())
                .deliveryDate(shipment.getDeliveryDate().toString())
                .deliveryFrom(shipment.getDeliveryFrom())
                .deliveryTo(shipment.getDeliveryTo())
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
}
