package org.enigma.tokonyadia_api.controller;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.service.impl.ProductServiceImpl;
import org.enigma.tokonyadia_api.service.impl.StoreServiceImpl;
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
    public Store createCustomer(@RequestBody Store store) {
        return storeService.create(store);
    }

    @GetMapping(path = "/{storeId}")
    public Store getCustomerById(@PathVariable String storeId) {
        return storeService.getById(storeId);
    }

    @PutMapping("/{storeId}")
    public Store updateCustomer(@PathVariable String storeId,@RequestBody Store store) {
        return storeService.update(storeId, store);
    }

    @DeleteMapping("/{storeId}")
    public String deleteCustomer(@PathVariable String storeId) {
        return storeService.delete(storeId);
    }

    @GetMapping
    public List<Store> getAllCustomers() {
        return storeService.getAll();
    }
}
