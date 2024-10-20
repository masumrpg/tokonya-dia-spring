package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.OrderDetail;

public interface OrderDetailService {
    OrderDetail create(OrderDetail request);

    void delete(OrderDetail request);
}
