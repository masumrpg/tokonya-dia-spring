package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.RegisterCreateRequest;
import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse create(RegisterCreateRequest request);
    CustomerResponse create(UserRequest request);
    Customer create(Customer customer);
    Customer getOneById(String id);
    CustomerResponse getById(String id);
    CustomerResponse update(String id, CustomerRequest customerRequest);
    void delete(String id);
    Page<CustomerResponse> getAll(SearchCommonRequest request);
}
