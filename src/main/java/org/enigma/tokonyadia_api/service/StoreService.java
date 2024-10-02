package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;

import java.util.List;

public interface StoreService {
    Store create(Store store);
    Store getById(String id);
    Store update(String id, Store store);
    String delete(String id);
    List<Store> getAll();
}
