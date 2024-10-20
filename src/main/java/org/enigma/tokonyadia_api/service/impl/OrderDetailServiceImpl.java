package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.repository.OrderDetailRepository;
import org.enigma.tokonyadia_api.service.OrderDetailService;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderDetail create(OrderDetail request) {
        validationUtil.validate(request);
        OrderDetail orderDetail = OrderDetail.builder()
                .order(request.getOrder())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        orderDetailRepository.saveAndFlush(orderDetail);
        return orderDetail;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(OrderDetail request) {
        orderDetailRepository.delete(request);
    }
}
