package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.repository.OrderDetailRepository;
import org.enigma.tokonyadia_api.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail create(OrderDetail request) {
        OrderDetail orderDetail = OrderDetail.builder()
                .order(request.getOrder())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        orderDetailRepository.saveAndFlush(orderDetail);
        return orderDetail;
    }

    @Override
    public OrderDetail getById(String id) {
        return null;
    }

    @Override
    public List<OrderDetail> getAll() {
        return List.of();
    }
}
