package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.PRODUCT_CATEGORY_API)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<?> createProductCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, "Successfully created product category", productCategoryResponse);
    }

    @GetMapping("/{productCategoryId}")
    public ResponseEntity<?> getProductCategory(@PathVariable String productCategoryId) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.getById(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully get product category", productCategoryResponse);
    }

    @PutMapping("/{productCategoryId}")
    public ResponseEntity<?> updateProductCategory(@PathVariable String productCategoryId, @RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.update(productCategoryId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully update product category", productCategoryResponse);
    }

    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable String productCategoryId) {
        productCategoryService.delete(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully delete product category", null);
    }

    @GetMapping
    public ResponseEntity<?> getAllProductCategory(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchCommonRequest searchCommonRequest = SearchCommonRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sort)
                .query(query)
                .build();
        Page<ProductCategoryResponse> productCategoryServiceAll = productCategoryService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT_CATEGORY, productCategoryServiceAll);
    }
}
