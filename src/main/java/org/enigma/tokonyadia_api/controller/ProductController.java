package org.enigma.tokonyadia_api.controller;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.service.impl.CustomerServiceImpl;
import org.enigma.tokonyadia_api.service.impl.ProductServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.PRODUCT_API)
public class ProductController {
    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping
    public Product createCustomer(@RequestBody Product product) {
        return productServiceImpl.create(product);
    }

    @GetMapping(path = "/{productId}")
    public Product getCustomerById(@PathVariable String productId) {
        return productServiceImpl.getById(productId);
    }

    @PutMapping("/{productId}")
    public Product updateCustomer(@PathVariable String productId,@RequestBody Product product) {
        return productServiceImpl.update(productId, product);
    }

    @DeleteMapping("/{productId}")
    public String deleteCustomer(@PathVariable String productId) {
        return productServiceImpl.delete(productId);
    }

    @GetMapping
    public List<Product> getAllCustomers() {
        return productServiceImpl.getAll();
    }
}
