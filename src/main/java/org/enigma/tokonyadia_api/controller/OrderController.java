package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderDetailResponse;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.ORDER_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(path = "/draft")
    public ResponseEntity<?> createDraft(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_ORDER, orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        OrderResponse oderResponse = orderService.getByOrderId(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER, oderResponse);
    }

    @PostMapping("/{orderId}/details/add")
    public ResponseEntity<?> addOrderDetailsById(@PathVariable String orderId, @RequestBody OrderDetailRequest request) {
        OrderResponse orderDetailResponseList = orderService.addOrderDetailByOrderId(orderId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_ORDER_DETAIL, orderDetailResponseList);
    }

    @PostMapping("/{orderId}/details/decrease")
    public ResponseEntity<?> decreaseOrderById(@PathVariable String orderId, @RequestBody OrderDetailRequest request) {
        OrderResponse orderDetailResponses = orderService.decreaseOrderDetailByOrderId(orderId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DECREASE_ORDER_DETAIL, orderDetailResponses);
    }

    @DeleteMapping("/{orderId}/details/{orderDetailId}")
    public ResponseEntity<?> removeOrderDetailById(@PathVariable String orderId, @PathVariable String orderDetailId) {
        OrderResponse orderResponse = orderService.removeOrderDetailByOrderId(orderId, orderDetailId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully remove detail order", orderResponse);
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getAllOrderDetailsById(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetailResponseList = orderService.getAllDetailByOrderId(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_DETAIL, orderDetailResponseList);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchCommonRequest searchCommonRequest = SearchCommonRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sort)
                .query(query)
                .build();
        Page<OrderResponse> orders = orderService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ORDER, orders);
    }
}
