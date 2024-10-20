package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Invoice;
import org.enigma.tokonyadia_api.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
    List<Invoice> findByPerson(Person person);
}
