package org.enigma.tokonyadia_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.TransactionRequest;
import org.enigma.tokonyadia_api.dto.response.TransactionResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.enigma.tokonyadia_api.repository.TransactionRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.TransactionDetailService;
import org.enigma.tokonyadia_api.service.TransactionService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.MapperUtil;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.*;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final CustomerService customerService;
    private final TransactionDetailService transactionDetailService;
    private final TransactionRepository transactionRepository;

    @Transactional(rollbackOn = Exception.class)
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
        return toTransactionResponse(newTransaction);
    }

    @Override
    public TransactionResponse getById(String id) {
        Optional<Transaction> byId = transactionRepository.findById(id);
        if (byId.isPresent()) {
            Transaction transaction = byId.get();
            List<TransactionDetail> transactionResponseList = new ArrayList<>(transaction.getTransactionDetails());
            transaction.setTransactionDetails(transactionResponseList);
            return toTransactionResponse(transaction);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
    }

    @Override
    public Page<TransactionResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Transaction> specification = new FilterSpecificationBuilder<Transaction>()
                .withLike("name", request.getQuery())
                .withEqual("siup", request.getQuery())
                .withEqual("phoneNumber", request.getQuery())
                .build();
        Page<Transaction> resultPage = transactionRepository.findAll(specification, pageable);

        return resultPage.map(MapperUtil::toTransactionResponse);
    }
}
