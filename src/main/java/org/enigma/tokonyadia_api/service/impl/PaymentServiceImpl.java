package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.dto.request.PaymentRequest;
import org.enigma.tokonyadia_api.dto.response.PaymentResponse;
import org.enigma.tokonyadia_api.entity.Payment;
import org.enigma.tokonyadia_api.service.PaymentService;

// TODO connect to midtrans: bayar 1 kali order, bagi ke setiap toko berdasarkan jumlah produk di tokonya
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponse create(PaymentRequest paymentRequest) {
        return null;
    }

    @Override
    public PaymentResponse getById(String id) {
        return null;
    }

    @Override
    public Payment getOne(String id) {
        return null;
    }
}
