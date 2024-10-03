package org.enigma.tokonyadia_api.controller;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.CustomerRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.service.impl.CustomerServiceImpl;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.CUSTOMER_API)
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerServiceImpl.create(customerRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_CUSTOMER, customerResponse);
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
        CustomerResponse customerResponse = customerServiceImpl.getById(customerId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_CUSTOMER, customerResponse);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String customerId,@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerServiceImpl.update(customerId, customerRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_CUSTOMER, customerResponse);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        customerServiceImpl.delete(customerId);
        return ResponseUtil.buildCommonResponse(HttpStatus.NO_CONTENT, Constant.SUCCESS_DELETE_CUSTOMER,null);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        Page<CustomerResponse> customerResponsePage = customerServiceImpl.getAll(page, size, sort);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_CUSTOMER, customerResponsePage);
    }
}
