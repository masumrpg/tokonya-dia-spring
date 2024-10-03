package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customerRequest);
    CustomerResponse getById(String id);
    CustomerResponse update(String id, CustomerRequest customerRequest);
    void delete(String id);
    Page<CustomerResponse> getAll(Integer page, Integer size, String sort);
}
