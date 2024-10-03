package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(Transaction transaction);
    Transaction getById(String id);
    Transaction update(String id, Transaction transaction);
    String delete(String id);
    List<Transaction> getAll();
}
