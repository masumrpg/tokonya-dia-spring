package org.enigma.tokonyadia_api.controller;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.service.impl.ProductServiceImpl;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createCustomer(@RequestBody ProductRequest product) {
        ProductResponse productResponse = productServiceImpl.create(product);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_PRODUCT, productResponse);
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String productId) {
        ProductResponse byId = productServiceImpl.getById(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT, byId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String productId,@RequestBody ProductRequest product) {
        ProductResponse productResponse = productServiceImpl.update(productId, product);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT, productResponse);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String productId) {
        productServiceImpl.delete(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.NO_CONTENT, Constant.SUCCESS_DELETE_PRODUCT, null);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        Page<ProductResponse> productResponsePage = productServiceImpl.getAll(page, size, sort);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT, productResponsePage);
    }
}
