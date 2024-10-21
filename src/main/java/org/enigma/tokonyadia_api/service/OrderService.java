package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.constant.OrderStatus;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import org.enigma.tokonyadia_api.dto.response.ProductDetailResponse;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    OrderResponse getByOrderId(String orderId);

    OrderResponse addOrderDetailByOrderId(String orderId, OrderDetailRequest request);

    OrderResponse decreaseOrderDetailByOrderId(String orderId, OrderDetailRequest request);

    OrderResponse removeOrderDetail(String orderId, String detailId);

    OrderResponse checkoutOrder(String orderId);

    OrderResponse cancelOrder(String orderId);

    void updateOrderStatus(String orderId, UpdateOrderStatusRequest request);

    void updateOrderStatus(String orderId, OrderStatus orderStatus);

    Order getOne(String id);

    List<ProductDetailResponse> getAllDetailByOrderId(String orderId);

    Page<OrderResponse> getAll(SearchCommonRequest request);

}
