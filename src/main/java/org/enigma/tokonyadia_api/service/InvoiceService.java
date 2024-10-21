package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.response.InvoiceResponse;
import org.enigma.tokonyadia_api.entity.Invoice;
import org.enigma.tokonyadia_api.entity.Order;

public interface InvoiceService {
    boolean existByOrder(Order order);

    InvoiceResponse findById(String invoiceId);

    Invoice getOne(String invoiceId);
}
