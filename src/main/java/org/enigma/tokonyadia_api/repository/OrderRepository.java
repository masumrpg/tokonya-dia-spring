package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Order;
import org.enigma.tokonyadia_api.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    List<Order> findByPerson(Person person);
}
