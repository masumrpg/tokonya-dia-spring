package org.enigma.tokonyadia_api.controller;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.CustomerResponse;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.service.impl.ProductServiceImpl;
import org.enigma.tokonyadia_api.service.impl.StoreServiceImpl;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.STORE_API)
public class StoreController {
    private final StoreServiceImpl storeService;

    public StoreController(StoreServiceImpl storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.create(storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_STORE, storeResponse);
    }

    @GetMapping(path = "/{storeId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String storeId) {
        StoreResponse storeResponse = storeService.getById(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_STORE, storeResponse);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String storeId, @RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.update(storeId, storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_STORE, storeResponse);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String storeId) {
        storeService.delete(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.NO_CONTENT, Constant.SUCCESS_DELETE_STORE, null);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        Page<StoreResponse> customerResponsePage = storeService.getAll(page, size, sort);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_CUSTOMER, customerResponsePage);
    }
}
