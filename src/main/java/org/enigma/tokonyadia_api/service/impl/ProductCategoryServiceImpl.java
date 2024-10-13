package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.enigma.tokonyadia_api.repository.ProductCategoryRepository;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.springframework.stereotype.Service;

// TODO Product Category
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategoryResponse create(ProductCategoryRequest request) {
        ProductCategory productCategory = ProductCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        productCategoryRepository.saveAndFlush(productCategory);
        return toCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse getById(String id) {
        return null;
    }

    @Override
    public ProductCategory getOne(String id) {
        return null;
    }

    @Override
    public ProductCategoryResponse update(String id, ProductCategoryRequest request) {
        return null;
    }

    @Override
    public ProductCategoryResponse delete(String id) {
        return null;
    }

    public ProductCategoryResponse toCategoryResponse(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .description(productCategory.getDescription())
                .build();
    }
}
