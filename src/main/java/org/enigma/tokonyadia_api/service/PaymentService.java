package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.PaymentRequest;
import org.enigma.tokonyadia_api.dto.response.MidtransNotificationRequest;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);

    void getNotification(MidtransNotificationRequest request);

    PaymentResponse getPaymentStatusByOrderId(String orderId);
}
