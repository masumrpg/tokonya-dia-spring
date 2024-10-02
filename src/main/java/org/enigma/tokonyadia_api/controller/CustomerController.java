package org.enigma.tokonyadia_api.controller;

import lombok.AllArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.service.impl.CustomerServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.CUSTOMER_API)
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerServiceImpl.create(customer);
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomerById(@PathVariable String customerId) {
        return customerServiceImpl.getById(customerId);
    }

    @PutMapping("/{customerId}")
    public Customer updateCustomer(@PathVariable String customerId,@RequestBody Customer customer) {
        return customerServiceImpl.update(customerId, customer);
    }

    @DeleteMapping("/{customerId}")
    public String deleteCustomer(@PathVariable String customerId) {
        return customerServiceImpl.delete(customerId);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerServiceImpl.getAll();
    }
}
