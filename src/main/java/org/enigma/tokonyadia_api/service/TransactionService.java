package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.TransactionRequest;
import org.enigma.tokonyadia_api.dto.response.TransactionResponse;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    TransactionResponse getById(String id);
    Page<TransactionResponse> getAll(SearchCommonRequest request);
}
