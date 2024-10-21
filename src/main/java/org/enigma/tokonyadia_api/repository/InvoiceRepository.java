package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Invoice;
import org.enigma.tokonyadia_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceRepository extends JpaRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
    boolean existsByOrder(Order order);
}
