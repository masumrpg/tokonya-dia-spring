package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.OrderResponse;
import org.enigma.tokonyadia_api.service.OrderService;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.ORDER_API)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_ORDER, orderResponse);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactionsById(@PathVariable String transactionId) {
        OrderResponse transaction = orderService.getById(transactionId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER, transaction);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(
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
        Page<OrderResponse> transactions = orderService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ORDER, transactions);
    }
}
