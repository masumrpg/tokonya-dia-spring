package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
