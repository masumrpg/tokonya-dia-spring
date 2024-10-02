package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
}
