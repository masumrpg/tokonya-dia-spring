package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.PaymentRequest;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;
import org.enigma.tokonyadia_api.entity.Payment;

public interface PaymentService {
    PaymentResponse create(PaymentRequest paymentRequest);
    PaymentResponse getById(String id);
    Payment getOne(String id);
}
