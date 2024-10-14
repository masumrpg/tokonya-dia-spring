package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.repository.OrderDetailRepository;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> create(Order order, List<OrderDetailRequest> request) {
        List<OrderDetail> orderDetailsList = new ArrayList<>();

        for (OrderDetailRequest orderValue : request) {
            // find product untuk mengecek stock product
            Product product = productService.getOneById(orderValue.getProductId());
            if (product.getStock() < orderValue.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity");
            }

            // update product
            productService.update(
                    product.getId(), ProductRequest.builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .stock(product.getStock() - orderValue.getQuantity())
                            .storeId(product.getStore().getId())
                            .build()
            );

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(orderValue.getQuantity())
                    .price(product.getPrice())
                    .build();
            orderDetailsList.add(orderDetail);
        }
        orderDetailRepository.saveAllAndFlush(orderDetailsList);
        return orderDetailsList;
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
