package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.repository.CustomerRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        verifyByPhoneNumber(customerRequest.getPhoneNumber());
        verifyByEmail(customerRequest.getEmail());
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .phoneNumber(customerRequest.getPhoneNumber())
                .email(customerRequest.getEmail())
                .address(customerRequest.getAddress())
                .build();
        customerRepository.saveAndFlush(customer);
        return toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getById(String id) {
        return toCustomerResponse(getOne(id));
    }

    @Override
    public CustomerResponse update(String id, CustomerRequest customerRequest) {
        verifyByPhoneNumber(customerRequest.getPhoneNumber());
        verifyByEmail(customerRequest.getEmail());
        Customer customer = getOne(id);
        customer.setName(customerRequest.getName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());
        customerRepository.save(customer);
        return toCustomerResponse(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = getOne(id);
        customerRepository.delete(customer);
    }

    @Override
    public Page<CustomerResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Customer> specification = new FilterSpecificationBuilder<Customer>()
                .withLike("name", request.getQuery())
                .withEqual("phoneNumber", request.getQuery())
                .build();
        Page<Customer> resultPage = customerRepository.findAll(specification, pageable);

        return resultPage.map(customer -> toCustomerResponse(customer));
    }

    private Customer getOne(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
    }

    private void verifyByPhoneNumber(String phoneNumber) {
        Optional<Customer> byPhone = customerRepository.findByPhoneNumber(phoneNumber);
        if (byPhone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone with " + phoneNumber + " already exists!");
        }
    }

    private void verifyByEmail(String email) {
        Optional<Customer> byEmail = customerRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email with " + email + " already exists!");
        }
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .build();
    }
}
