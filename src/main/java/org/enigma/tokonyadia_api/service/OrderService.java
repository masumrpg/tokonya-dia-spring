package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse getByOrderId(String orderId);
    List<OrderDetailResponse> addOrderDetailByOrderId(String orderId, OrderDetailRequest request);
    Order getOne(String id);
    List<OrderDetailResponse> getDetailByOrderId(String orderId);
    Page<OrderResponse> getAll(SearchCommonRequest request);
}
