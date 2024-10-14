package org.enigma.tokonyadia_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.OrderStatus;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.OrderDetail;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.repository.OrderRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.OrderDetailService;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.service.ProductService;
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
    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse create(OrderRequest request) {
        Person person = personService.getOneById(request.getPersonId());

        Optional<Order> optionalOrder = orderRepository.findByPerson(person);
        if (optionalOrder.isPresent()) {
            if (optionalOrder.get().getStatus().equals(OrderStatus.DRAFT)) throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_ORDER_ALREADY_EXISTS);;
        }

        Order newOrder = Order.builder()
                .person(person)
                .status(OrderStatus.DRAFT)
                .build();
        orderRepository.saveAndFlush(newOrder);

        return toOrderResponse(newOrder);
    }

    @Override
    public OrderResponse getByOrderId(String orderId) {
        return MapperUtil.toOrderResponse(getOne(orderId));
    }

    // FIXME masih error null get name
    @Override
    public OrderResponse createDetailByOrderId(String orderId, OrderDetailRequest request) {
        Product product = productService.getOneById(request.getProductId());

        Order order = getOne(orderId);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        Optional<OrderDetail> existingOrderDetail = order.getOrderDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingOrderDetail.isPresent()) {
            OrderDetail orderDetail = existingOrderDetail.get();
            orderDetail.setQuantity(orderDetail.getQuantity() + request.getQuantity());
            orderDetail.setPrice(product.getPrice());
        } else {
            OrderDetail newOrderDetail = OrderDetail.builder()
                    .product(product)
                    .order(order)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            order.getOrderDetails().add(newOrderDetail);
        }

        Order updatedOrder = orderRepository.save(order);
        return toOrderResponse(updatedOrder);
    }

    @Override
    public Order getOne(String orderId) {
        Optional<Order> byId = orderRepository.findById(orderId);
        if (byId.isPresent()) {
            Order order = byId.get();
            List<OrderDetail> orderDetailList = new ArrayList<>(order.getOrderDetails());
            order.setOrderDetails(orderDetailList);
            return order;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ORDER_NOT_FOUND);
    }

    @Override
    public List<OrderDetailResponse> getDetailByOrderId(String orderId) {
        Order order = getOne(orderId);
        List<OrderDetail> orderDetailList = new ArrayList<>(order.getOrderDetails());
        return orderDetailList.stream().map(MapperUtil::toOrderDetailResponse).toList();
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

        return resultPage.map(MapperUtil::toOrderResponse);
    }
}
