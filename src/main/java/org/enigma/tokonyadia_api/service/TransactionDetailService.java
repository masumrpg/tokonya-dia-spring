package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> create(Transaction transaction, List<TransactionDetailRequest> request);
    TransactionDetail getById(String id);
    List<TransactionDetail> getAll();
}
