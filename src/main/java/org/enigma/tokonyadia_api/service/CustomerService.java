package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customerRequest);
    CustomerResponse getById(String id);
    CustomerResponse update(String id, CustomerRequest customerRequest);
    void delete(String id);
    Page<CustomerResponse> getAll(SearchCustomerRequest request);
}
