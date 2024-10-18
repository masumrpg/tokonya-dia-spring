package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.domain.Page;

public interface StoreService {
    StoreResponse create(StoreRequest request);

    Store getOneById(String id);

    StoreResponse getById(String id);

    StoreResponse update(String id, StoreRequest request);

    void delete(String id);

    Page<StoreResponse> getAll(SearchCommonRequest request);
}
