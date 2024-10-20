package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRatingRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRatingRequest;
import org.enigma.tokonyadia_api.dto.response.ProductRatingResponse;
import org.enigma.tokonyadia_api.service.ProductRatingService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.PRODUCT_RATING_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product Rating Management", description = "APIs for managing product ratings")
public class ProductRatingController {
    private final ProductRatingService productRatingService;

    /**
     * Create a new product rating.
     *
     * @param request the product rating details
     * @return the response entity with created product rating details
     */
    @Operation(summary = "Create Product Rating", description = "Create a new product rating")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PRODUCT_RATING, productRatingResponse);
    }

    /**
     * Get a product rating by ID.
     *
     * @param ratingId the ID of the product rating
     * @return the response entity with product rating details
     */
    @Operation(summary = "Get Product Rating by ID", description = "Retrieve a product rating by its ID")
    @GetMapping("/{ratingId}")
    public ResponseEntity<?> getProductRating(@PathVariable String ratingId) {
        ProductRatingResponse productRatingResponse = productRatingService.getById(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_RATING, productRatingResponse);
    }

    /**
     * Update an existing product rating by ID.
     *
     * @param ratingId the ID of the product rating
     * @param request  the updated product rating details
     * @return the response entity with updated product rating details
     */
    @Operation(summary = "Update Product Rating", description = "Update an existing product rating by its ID")
    @PutMapping("/{ratingId}")
    public ResponseEntity<?> updateProduct(@PathVariable String ratingId, @RequestBody UpdateProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.update(ratingId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_RATING, productRatingResponse);
    }

    /**
     * Delete a product rating by ID.
     *
     * @param ratingId the ID of the product rating to delete
     * @return the response entity indicating success
     */
    @Operation(summary = "Delete Product Rating", description = "Delete a product rating by its ID")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ratingId) {
        productRatingService.delete(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_DELETE_RATING, null);
    }

    /**
     * Get all product ratings with pagination and filtering options.
     *
     * @param page     the page number (default: 1)
     * @param size     the page size (default: 10)
     * @param sort     the sort field
     * @param query    the search query
     * @param minPrice the minimum price filter
     * @param maxPrice the maximum price filter
     * @return the response entity with a paginated list of product ratings
     */
    @Operation(summary = "Get All Product Ratings", description = "Retrieve all product ratings with pagination and filtering")
    @GetMapping
    public ResponseEntity<?> getAllProductRating(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice
    ) {
        SearchWithMinMaxRequest searchWithMinMaxRequest = SearchWithMinMaxRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sort)
                .query(query)
                .minValue(minPrice)
                .maxValue(maxPrice)
                .build();
        Page<ProductRatingResponse> responsePage = productRatingService.getAll(searchWithMinMaxRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT_RATING, responsePage);
    }
}
