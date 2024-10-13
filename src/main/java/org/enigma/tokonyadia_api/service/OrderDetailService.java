package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> create(Order order, List<OrderDetailRequest> request);
    OrderDetail getById(String id);
    List<OrderDetail> getAll();
}
