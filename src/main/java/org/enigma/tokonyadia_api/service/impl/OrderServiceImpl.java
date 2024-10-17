package org.enigma.tokonyadia_api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.enigma.tokonyadia_api.util.MapperUtil.*;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse create(OrderRequest request) {
        Person person = personService.getOneById(request.getPersonId());

        Optional<Order> optionalOrder = orderRepository.findByPerson(person);
        if (optionalOrder.isPresent()) {
            if (optionalOrder.get().getStatus().equals(OrderStatus.DRAFT))
                throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_ORDER_ALREADY_EXISTS);
        }

        Order newOrder = Order.builder()
                .person(person)
                .status(OrderStatus.DRAFT)
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
        Order order = getOne(orderId);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        Product product = productService.getOneById(request.getProductId());

        Optional<OrderDetail> existingOrderDetail = order.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getProduct().getId().equals(product.getId()))
                .findFirst();

        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

        if (existingOrderDetail.isPresent()) {
            OrderDetail orderDetail = existingOrderDetail.get();

            if (orderDetail.getQuantity() + 1 > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product stock is not enough");
            }

            orderDetail.setOrder(order);
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            orderDetail.setPrice(product.getPrice());
            orderDetailResponses.add(toOrderDetailResponse(orderDetail));
        } else {
            if (1 > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product stock is not enough");
            }

            OrderDetail newOrderDetail = OrderDetail.builder()
                    .product(product)
                    .order(order)
                    .quantity(1)
                    .price(product.getPrice())
                    .build();

            OrderDetail orderDetailAdded = orderDetailService.create(newOrderDetail);
            order.getOrderDetails().add(orderDetailAdded);
            orderDetailResponses.add(toOrderDetailResponse(orderDetailAdded));

            orderRepository.saveAndFlush(order);
        }

        return MapperUtil.toOrderResponse(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse decreaseOrderDetailByOrderId(String orderId, OrderDetailRequest request) {
        Order order = getOne(orderId);
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        Product product = productService.getOneById(request.getProductId());

        order.getOrderDetails().forEach(orderDetail -> {
            if (orderDetail.getProduct().getId().equals(product.getId())) {
                if (orderDetail.getQuantity() == 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product can't decrease again");
                } else {
                    orderDetail.setQuantity(orderDetail.getQuantity() - 1);
                    orderDetail.setPrice(product.getPrice());
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order detail not found");
            }
        });

        return MapperUtil.toOrderResponse(order);
    }

    // FIXME ga bisa di hapus yang di db
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse removeOrderDetailByOrderId(String orderId, String orderDetailId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.ERROR_ADD_ITEMS_TO_NON_DRAFT);
        }

        order.getOrderDetails().removeIf(orderDetail -> orderDetail.getId().equals(orderDetailId));

        orderRepository.saveAndFlush(order);

        return MapperUtil.toOrderResponse(order);
    }

    // TODO checkout

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<OrderDetailResponse> getAllDetailByOrderId(String orderId) {
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
