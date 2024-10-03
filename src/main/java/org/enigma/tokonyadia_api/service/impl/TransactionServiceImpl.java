package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.entity.Transaction;
import org.enigma.tokonyadia_api.repository.TransactionRepository;
import org.enigma.tokonyadia_api.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {

        return null;
    }

    @Override
    public Transaction getById(String id) {
        return null;
    }

    @Override
    public Transaction update(String id, Transaction transaction) {
        return null;
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<Transaction> getAll() {
        return List.of();
    }
}
