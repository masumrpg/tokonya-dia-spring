package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product Category Management", description = "APIs for managing product categories")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    /**
     * Create a new product category.
     *
     * @param request the product category request
     * @return the response entity with product category details
     */
    @Operation(summary = "Create Product Category", description = "Create a new product category")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProductCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, "Successfully created product category", productCategoryResponse);
    }

    /**
     * Get a product category by ID.
     *
     * @param productCategoryId the ID of the product category
     * @return the response entity with product category details
     */
    @Operation(summary = "Get Product Category", description = "Retrieve a product category by its ID")
    @GetMapping("/{productCategoryId}")
    public ResponseEntity<?> getProductCategory(@PathVariable String productCategoryId) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.getById(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully retrieved product category", productCategoryResponse);
    }

    /**
     * Update a product category by ID.
     *
     * @param productCategoryId the ID of the product category
     * @param request           the updated product category request
     * @return the response entity with updated product category details
     */
    @Operation(summary = "Update Product Category", description = "Update an existing product category by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productCategoryId}")
    public ResponseEntity<?> updateProductCategory(@PathVariable String productCategoryId, @RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.update(productCategoryId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully updated product category", productCategoryResponse);
    }

    /**
     * Delete a product category by ID.
     *
     * @param productCategoryId the ID of the product category to delete
     * @return the response entity indicating success
     */
    @Operation(summary = "Delete Product Category", description = "Delete a product category by its ID")
    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable String productCategoryId) {
        productCategoryService.delete(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully deleted product category", null);
    }

    /**
     * Get all product categories with pagination and filtering options.
     *
     * @param page  the page number (default: 1)
     * @param size  the page size (default: 10)
     * @param sort  the sort field
     * @param query the search query
     * @return the response entity with a paginated list of product categories
     */
    @Operation(summary = "Get All Product Categories", description = "Retrieve all product categories with pagination and filtering")
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
