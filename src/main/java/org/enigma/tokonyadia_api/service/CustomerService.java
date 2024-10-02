package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer customer);
    Customer getById(String id);
    Customer update(String id, Customer customer);
    String delete(String id);
    List<Customer> getAll();
}
