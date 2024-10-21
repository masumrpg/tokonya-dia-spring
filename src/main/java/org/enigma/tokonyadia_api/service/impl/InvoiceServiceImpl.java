package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.response.InvoiceResponse;
import org.enigma.tokonyadia_api.entity.Invoice;
import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.repository.InvoiceRepository;
import org.enigma.tokonyadia_api.service.InvoiceService;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public boolean existByOrder(Order order) {
        return invoiceRepository.existsByOrder(order);
    }

    @Override
    public InvoiceResponse findById(String invoiceId) {
        return MapperUtil.toInvoiceResponse(getOne(invoiceId));
    }

    @Override
    public Invoice getOne(String invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_INVOICE_NOT_FOUND));
    }

    // TODO get all invoice
}
