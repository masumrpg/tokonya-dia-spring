package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest productRequest);
    ProductResponse getById(String id);
    ProductResponse update(String id, ProductRequest product);
    void delete(String id);
    Page<ProductResponse> getAll(Integer page,
                                 Integer size,
                                 String sort);
}
