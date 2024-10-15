package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail create(OrderDetail request);

    OrderDetail getById(String id);

    List<OrderDetail> getAll();
}
