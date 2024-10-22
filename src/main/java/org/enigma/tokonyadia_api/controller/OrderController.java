package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.OrderDetailRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.OrderRequest;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
import org.enigma.tokonyadia_api.dto.response.ProductDetailResponse;
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
@Tag(name = "Order", description = "APIs for managing orders")
@Slf4j
public class OrderController {

    private static class CommonResponseOrderResponse extends CommonResponse<OrderResponse> {
    }

    private static class CommonResponseProductDetailResponse extends CommonResponse<ProductDetailResponse> {
    }

    private final OrderService orderService;

    @Operation(summary = "Create Order Draft",
            description = "Create a new draft order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created draft order", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @PostMapping(path = "/draft")
    public ResponseEntity<?> createDraft(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_ORDER, orderResponse);
    }

    @Operation(summary = "Get Order by ID",
            description = "Retrieve an order by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved order by id", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@Parameter(description = "ID of the order to retrieve") @PathVariable String orderId) {
        OrderResponse orderResponse = orderService.getByOrderId(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER, orderResponse);
    }

    @Operation(summary = "Add Order Details",
            description = "Add details to an existing order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully added order details", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @PostMapping("/{orderId}/details/add")
    public ResponseEntity<?> addOrderDetailsById(
            @Parameter(description = "ID of the order to which details will be added") @PathVariable String orderId,
            @RequestBody OrderDetailRequest request) {
        OrderResponse orderDetailResponseList = orderService.addOrderDetailByOrderId(orderId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_ORDER_DETAIL, orderDetailResponseList);
    }

    @Operation(summary = "Decrease Order Details",
            description = "Decrease details in an existing order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully decreased order details", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @PostMapping("/{orderId}/details/decrease")
    public ResponseEntity<?> decreaseOrderById(
            @Parameter(description = "ID of the order to decrease details from") @PathVariable String orderId,
            @RequestBody OrderDetailRequest request) {
        OrderResponse orderDetailResponses = orderService.decreaseOrderDetailByOrderId(orderId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DECREASE_ORDER_DETAIL, orderDetailResponses);
    }

    @Operation(summary = "Remove Order Detail",
            description = "Remove a specific detail from an order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed order detail", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @DeleteMapping("/{orderId}/details/{orderDetailId}")
    public ResponseEntity<?> removeOrderDetailById(
            @Parameter(description = "ID of the order") @PathVariable String orderId,
            @Parameter(description = "ID of the order detail to remove") @PathVariable String orderDetailId) {
        log.error(orderId);
        log.error(orderDetailId);
        OrderResponse orderResponse = orderService.removeOrderDetail(orderId, orderDetailId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully removed order detail", orderResponse);
    }

    @Operation(summary = "Checkout Order",
            description = "Checkout an existing order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully checked out the order", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @PostMapping("/{orderId}/checkout")
    public ResponseEntity<?> checkoutOrder(@Parameter(description = "ID of the order to checkout") @PathVariable String orderId) {
        OrderResponse orderResponse = orderService.checkoutOrder(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_CHECKOUT_ORDER, orderResponse);
    }

    @Operation(summary = "Cancel Order",
            description = "Cancel an existing order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully canceled the order", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@Parameter(description = "ID of the order to cancel") @PathVariable String orderId) {
        OrderResponse orderResponse = orderService.cancelOrder(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_CANCEL_ORDER, orderResponse);
    }

    @Operation(summary = "Get All Order Details",
            description = "Retrieve all details for a specific order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all order details", content = @Content(schema = @Schema(implementation = CommonResponseProductDetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getAllOrderDetailsById(@Parameter(description = "ID of the order to get details for") @PathVariable String orderId) {
        List<ProductDetailResponse> productDetailResponseList = orderService.getAllDetailByOrderId(orderId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ORDER_DETAIL, productDetailResponseList);
    }

    @Operation(summary = "Get All Orders",
            description = "Retrieve all orders with pagination and filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders", content = @Content(schema = @Schema(implementation = CommonResponseOrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query) {
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
