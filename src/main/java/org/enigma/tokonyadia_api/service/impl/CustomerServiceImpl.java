package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.CustomerRepository;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.UserService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.utils.MapperUtil;
import org.enigma.tokonyadia_api.utils.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.toCustomerResponse;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        // verify
        checkCustomerByEmail(request.getEmail());
        checkCustomerByPhone(request.getPhoneNumber());

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        userService.create(userAccount);
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userAccount(userAccount)
                .build();
        customerRepository.saveAndFlush(customer);
        return toCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getOneById(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse getById(String id) {
        return toCustomerResponse(getOneById(id));
    }

    @Override
    public CustomerResponse update(String id, CustomerRequest customerRequest) {
        checkCustomerByPhone(customerRequest.getPhoneNumber());
        checkCustomerByEmail(customerRequest.getEmail());
        Customer customer = getOneById(id);
        customer.setName(customerRequest.getName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());
        customerRepository.save(customer);
        return toCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer customer = getOneById(id);
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

        return resultPage.map(MapperUtil::toCustomerResponse);
    }

    private void checkCustomerByPhone(String phoneNumber) {
        Optional<Customer> byPhone = customerRepository.findByPhoneNumber(phoneNumber);
        if (byPhone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone with " + phoneNumber + " already exists!");
        }
    }

    private void checkCustomerByEmail(String email) {
        Optional<Customer> byEmail = customerRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email with " + email + " already exists!");
        }
    }
}
