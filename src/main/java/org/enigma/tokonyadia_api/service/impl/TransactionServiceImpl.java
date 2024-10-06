package org.enigma.tokonyadia_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.TransactionRequest;
import org.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import org.enigma.tokonyadia_api.dto.response.TransactionResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.enigma.tokonyadia_api.repository.TransactionRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.TransactionDetailService;
import org.enigma.tokonyadia_api.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.toTransactionDetailResponse;
import static org.enigma.tokonyadia_api.utils.MapperUtil.toTransactionResponse;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final CustomerService customerService;
    private final TransactionDetailService transactionDetailService;
    private final TransactionRepository transactionRepository;

    // FIXME make this cant response transaction
    @Transactional
    @Override
    public TransactionResponse create(TransactionRequest request) {
        // get customer
        Customer customer = customerService.getOneById(request.getCustomerId());

        // create transaction
        Transaction newTransaction = Transaction.builder()
                .customer(customer)
                .build();
        transactionRepository.saveAndFlush(newTransaction);

        // create transaction detail
        List<TransactionDetail> transactionDetailList = transactionDetailService.create(newTransaction, request.getTransactionsDetails());

        // Response
        newTransaction.setTransactionDetails(transactionDetailList);
        List<TransactionDetailResponse> transactionDetailResponses = new ArrayList<>();
        for (TransactionDetail transactionDetail : transactionDetailList) {
            transactionDetailResponses.add(toTransactionDetailResponse(transactionDetail));
        }
        return toTransactionResponse(newTransaction, transactionDetailResponses);
    }

    @Override
    public TransactionResponse getById(String id) {
        List<TransactionDetailResponse> transactionResponseList = new ArrayList<>();
        Optional<Transaction> byId = transactionRepository.findById(id);
        if (byId.isPresent()) {
            Transaction transaction = byId.get();
            for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
                transactionResponseList.add(toTransactionDetailResponse(transactionDetail));
            }
            return toTransactionResponse(transaction, transactionResponseList);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    @Override
    public List<Transaction> getAll() {
        return List.of();
    }
}
