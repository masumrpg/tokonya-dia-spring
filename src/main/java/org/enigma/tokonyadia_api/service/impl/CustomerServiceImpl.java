package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.repository.CustomerRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        Optional<Customer> byPhone = customerRepository.findByPhoneNumber(customer.getPhoneNumber());
        if (byPhone.isPresent()) {
            throw new RuntimeException("Pelanggan dengan no hp " + customer.getPhoneNumber() + " sudah ada!");
        }
        Optional<Customer> byEmail = customerRepository.findByEmail(customer.getEmail());
        if (byEmail.isPresent()) {
            throw new RuntimeException("Pelanggan dengan email " + customer.getEmail() + " sudah ada!");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new RuntimeException("Pelanggan dengan id " + id + " tidak ada!");
    }

    @Override
    public Customer update(String id, Customer customer) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            Customer customerToUpdate = byId.get();
            customerToUpdate.setName(customer.getName());
            customerToUpdate.setAddress(customer.getAddress());
            customerToUpdate.setEmail(customer.getEmail());
            customerToUpdate.setPhoneNumber(customer.getPhoneNumber());
            return customerRepository.save(customerToUpdate);
        }
        throw new RuntimeException("Pelanggan dengan id " + id + " tidak ada!");
    }

    @Override
    public String delete(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            customerRepository.delete(byId.get());
            return "Pelanggan dengan id " + id + " sukses dihapus!";
        }
        throw new RuntimeException("Pelanggan dengan id " + id + " tidak ada!");
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty()) {
            throw new RuntimeException("Pelanggan dengan tidak ada!");
        }
        return customerList;
    }
}
