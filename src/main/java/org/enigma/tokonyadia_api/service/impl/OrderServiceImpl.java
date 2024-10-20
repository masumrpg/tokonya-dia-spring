package org.enigma.tokonyadia_api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.OrderStatus;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateOrderStatusRequest;
import org.enigma.tokonyadia_api.dto.response.ProductDetailResponse;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.entity.*;
import org.enigma.tokonyadia_api.repository.OrderRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.enigma.tokonyadia_api.util.MapperUtil.*;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final ProductService productService;
    private final ValidationUtil validationUtil;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse create(OrderRequest request) {
        validationUtil.validate(request);
        Person person = personService.getOne(request.getPersonId());

        List<Order> orderList = orderRepository.findByPerson(person);

        for (Order order : orderList) {
            if (order != null && order.getOrderStatus().equals(OrderStatus.DRAFT))
                throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_ORDER_ALREADY_EXISTS);
        }

        Order newOrder = Order.builder()
                .person(person)
                .orderStatus(OrderStatus.DRAFT)
                .orderDetails(new ArrayList<>())
                .build();
        orderRepository.saveAndFlush(newOrder);

        return toOrderResponse(newOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getByOrderId(String orderId) {
        return MapperUtil.toOrderResponse(getOne(orderId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse addOrderDetailByOrderId(String orderId, OrderDetailRequest request) {
        validationUtil.validate(request);
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        Product product = productService.getOne(request.getProductId());

        Optional<OrderDetail> existingOrderDetail = order.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingOrderDetail.isPresent()) {
            OrderDetail orderDetail = existingOrderDetail.get();

            if (orderDetail.getQuantity() + 1 > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product stock is not enough");
            }

            orderDetail.setOrder(order);
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            orderDetail.setPrice(product.getPrice());
        } else {
            if (1 > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product stock is not enough");
            }

            OrderDetail newOrderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(1)
                    .price(product.getPrice())
                    .build();

            order.getOrderDetails().add(newOrderDetail);
        }

        orderRepository.saveAndFlush(order);

        return MapperUtil.toOrderResponse(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse decreaseOrderDetailByOrderId(String orderId, OrderDetailRequest request) {
        validationUtil.validate(request);

        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        Product product = productService.getOne(request.getProductId());

        OrderDetail orderDetail = order.getOrderDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order detail not found"));

        if (orderDetail.getQuantity() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product can't decrease again");
        }

        orderDetail.setQuantity(orderDetail.getQuantity() - 1);
        orderDetail.setPrice(product.getPrice());

        return MapperUtil.toOrderResponse(order);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse removeOrderDetail(String orderId, String detailId) {
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_REMOVE_ITEMS_FROM_NON_DRAFT);
        }
        log.error(order.getOrderDetails().get(0).getId());
        order.getOrderDetails().removeIf(orderDetail -> orderDetail.getId().equals(detailId));

        Order saved = orderRepository.saveAndFlush(order);
        return MapperUtil.toOrderResponse(saved);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse checkoutOrder(String orderId) {
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_CHECKOUT_NON_DRAFT);

        if (order.getOrderDetails().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ORDER_CANNOT_EMPTY);

        Person person = personService.getOne(order.getPerson().getId());

        Set<Store> uniqueStores = order.getOrderDetails().stream()
                .map(orderDetail -> orderDetail.getProduct().getStore())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        order.getOrderDetails().forEach(orderDetail -> uniqueStores.forEach(store -> {
            Shipment newShipment = Shipment.builder()
                    .deliveryFrom(store.getAddress())
                    .deliveryTo(person.getAddress())
                    .orderDetail(orderDetail)
                    .build();
            orderDetail.setShipment(newShipment);
        }));

        order.setOrderStatus(OrderStatus.PENDING);
        Order updatedOrder = orderRepository.saveAndFlush(order);

        return MapperUtil.toOrderResponse(updatedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse cancelOrder(String orderId) {
        Order order = getOne(orderId);
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ORDER_IS_NOT_PENDING);
        }

        order.getOrderDetails().forEach(orderDetail -> {
            log.error(orderDetail.getShipment().getId());
            if (orderDetail.getShipment().getId() != null) {
                orderDetail.setShipment(null);
            }
        });

        order.setOrderStatus(OrderStatus.DRAFT);

        orderRepository.saveAndFlush(order);
        return MapperUtil.toOrderResponse(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        validationUtil.validate(request);
        Order order = getOne(orderId);
        order.setOrderStatus(request.getStatus());
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOne(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ORDER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDetailResponse> getAllDetailByOrderId(String orderId) {
        Order order = getOne(orderId);
        List<OrderDetail> orderDetailList = new ArrayList<>(order.getOrderDetails());
        return orderDetailList.stream().map(MapperUtil::toOrderDetailResponse).toList();
    }


    @Transactional(readOnly = true)
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
