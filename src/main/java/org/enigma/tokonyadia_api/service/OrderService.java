package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse getById(String id);
    Page<OrderResponse> getAll(SearchCommonRequest request);
}
