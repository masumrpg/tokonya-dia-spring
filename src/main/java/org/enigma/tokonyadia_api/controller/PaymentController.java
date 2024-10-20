package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.PaymentRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransNotificationRequest;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;
import org.enigma.tokonyadia_api.service.PaymentService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = Constant.PAYMENT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Payment Management", description = "APIs for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Create a new payment.
     *
     * @param request the payment request
     * @return the response entity with payment details
     */
    @Operation(summary = "Create Payment", description = "Create a new payment")
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse payment = paymentService.createPayment(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_CHECKOUT, payment);
    }

    /**
     * Handle payment notifications from the payment gateway.
     *
     * @param request the notification data from the payment gateway
     * @return the response entity indicating success
     */
    @Operation(summary = "Handle Payment Notification", description = "Handle notifications from the payment gateway")
    @PostMapping(path = "/notifications")
    public ResponseEntity<?> handleNotification(
            @Parameter(description = "Notification data from payment gateway") @RequestBody Map<String, String> request) {
        MidtransNotificationRequest midtransNotificationRequest = MidtransNotificationRequest.builder()
                .orderId(request.get("order_id"))
                .transactionTime(request.get("transaction_time"))
                .grossAmount(request.get("gross_amount"))
                .statusCode(request.get("status_code"))
                .transactionStatus(request.get("transaction_status"))
                .signatureKey(request.get("signature_key"))
                .build();
        paymentService.getNotification(midtransNotificationRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.OK, null);
    }
}