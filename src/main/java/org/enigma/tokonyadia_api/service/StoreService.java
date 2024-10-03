package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreService {
    StoreResponse create(StoreRequest storeRequest);
    StoreResponse getById(String id);
    StoreResponse update(String id, StoreRequest store);
    void delete(String id);
    Page<StoreResponse> getAll(Integer page, Integer size, String sort);
}
