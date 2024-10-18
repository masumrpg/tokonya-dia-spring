package org.enigma.tokonyadia_api.controller;

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
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse payment = paymentService.createPayment(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_CHECKOUT, payment);
    }

    @PostMapping(path = "/notifications")
    public ResponseEntity<?> handleNotification(@RequestBody Map<String, String> request) {
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