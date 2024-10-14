package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.springframework.data.domain.Page;

public interface ProductCategoryService {
    ProductCategoryResponse create(ProductCategoryRequest request);
    ProductCategoryResponse getById(String id);
    ProductCategory getOne(String id);
    ProductCategoryResponse update(String id, ProductCategoryRequest request);
    void delete(String id);
    Page<ProductCategoryResponse> getAll(SearchCommonRequest request);
}