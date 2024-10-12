package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(ProductRequest productRequest);
    Product getOneById(String id);
    ProductResponse getById(String id);
    ProductResponse update(String id, ProductRequest product);
    void delete(String id);
    Page<ProductResponse> getAll(SearchWithMinMaxRequest request);
}
