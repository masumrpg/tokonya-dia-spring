package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.entity.ProductCategory;
import org.enigma.tokonyadia_api.repository.ProductCategoryRepository;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.enigma.tokonyadia_api.util.MapperUtil.toProductCategoryResponse;

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
        return toProductCategoryResponse(productCategory);
    }

    @Override
    public ProductCategoryResponse getById(String id) {
        return toProductCategoryResponse(getOne(id));
    }

    @Override
    public ProductCategory getOne(String id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        return optionalProductCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.PRODUCT_CATEGORY_NOT_FOUND));
    }

    @Override
    public ProductCategoryResponse update(String id, ProductCategoryRequest request) {
        ProductCategory productCategory = getOne(id);
        productCategory.setName(request.getName());
        productCategory.setDescription(request.getDescription());
        productCategoryRepository.save(productCategory);
        return toProductCategoryResponse(productCategory);
    }

    @Override
    public void delete(String id) {
        ProductCategory productCategory = getOne(id);
        productCategoryRepository.delete(productCategory);
    }

    @Override
    public Page<ProductCategoryResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<ProductCategory> specification = new FilterSpecificationBuilder<ProductCategory>()
                .withLike("name", request.getQuery())
                .build();
        Page<ProductCategory> resultPage = productCategoryRepository.findAll(specification, pageable);
        return resultPage.map(MapperUtil::toProductCategoryResponse);
    }
}
