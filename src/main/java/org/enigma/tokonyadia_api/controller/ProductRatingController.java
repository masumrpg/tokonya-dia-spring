package org.enigma.tokonyadia_api.controller;

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
public class ProductRatingController {
    private final ProductRatingService productRatingService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PRODUCT_RATING, productRatingResponse);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<?> getProductRating(@PathVariable String ratingId) {
        ProductRatingResponse productRatingResponse = productRatingService.getById(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_RATING, productRatingResponse);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<?> updateProduct(@PathVariable String ratingId, @RequestBody UpdateProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.update(ratingId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_RATING, productRatingResponse);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ratingId) {
        productRatingService.delete(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_DELETE_RATING, null);
    }

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
