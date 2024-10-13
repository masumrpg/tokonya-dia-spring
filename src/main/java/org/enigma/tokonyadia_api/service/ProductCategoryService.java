package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.entity.ProductCategory;

public interface ProductCategoryService {
    ProductCategoryResponse create(ProductCategoryRequest request);
    ProductCategoryResponse getById(String id);
    ProductCategory getOne(String id);
    ProductCategoryResponse update(String id, ProductCategoryRequest request);
    ProductCategoryResponse delete(String id);
}