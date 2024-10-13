package org.enigma.tokonyadia_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.repository.OrderRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.OrderDetailService;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.MapperUtil;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.*;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final CustomerService customerService;
    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse create(OrderRequest request) {
        // get customer
        Customer customer = customerService.getOneById(request.getCustomerId());

        // create transaction
        Order newOrder = Order.builder()
                .customer(customer)
                .build();
        orderRepository.saveAndFlush(newOrder);

        // create transaction detail
        List<OrderDetail> orderDetailList = orderDetailService.create(newOrder, request.getTransactionsDetails());

        // Response
        newOrder.setOrderDetails(orderDetailList);
        return toTransactionResponse(newOrder);
    }

    @Override
    public OrderResponse getById(String id) {
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isPresent()) {
            Order order = byId.get();
            List<OrderDetail> transactionResponseList = new ArrayList<>(order.getOrderDetails());
            order.setOrderDetails(transactionResponseList);
            return toTransactionResponse(order);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    @Override
    public Page<OrderResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Order> specification = new FilterSpecificationBuilder<Order>()
                .withLike("name", request.getQuery())
                .withEqual("siup", request.getQuery())
                .withEqual("phoneNumber", request.getQuery())
                .build();
        Page<Order> resultPage = orderRepository.findAll(specification, pageable);

        return resultPage.map(MapperUtil::toTransactionResponse);
    }
}
