package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRatingRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRatingRequest;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
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
@Tag(name = "Product Rating", description = "APIs for managing product ratings")
public class ProductRatingController {
    private static class CommonResponseProductRatingResponse extends CommonResponse<ProductRatingResponse> {
    }

    private final ProductRatingService productRatingService;


    @Operation(summary = "Create Product Rating",
            description = "Create a new product rating",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created product rating", content = @Content(schema = @Schema(implementation = CommonResponseProductRatingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PRODUCT_RATING, productRatingResponse);
    }

    @Operation(summary = "Get Product Rating by ID",
            description = "Retrieve a product rating by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved product rating", content = @Content(schema = @Schema(implementation = CommonResponseProductRatingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @GetMapping("/{ratingId}")
    public ResponseEntity<?> getProductRating(@PathVariable String ratingId) {
        ProductRatingResponse productRatingResponse = productRatingService.getById(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_RATING, productRatingResponse);
    }

    @Operation(summary = "Update Product Rating",
            description = "Update an existing product rating by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated product rating", content = @Content(schema = @Schema(implementation = CommonResponseProductRatingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PutMapping("/{ratingId}")
    public ResponseEntity<?> updateProduct(@PathVariable String ratingId, @RequestBody UpdateProductRatingRequest request) {
        ProductRatingResponse productRatingResponse = productRatingService.update(ratingId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_RATING, productRatingResponse);
    }

    @Operation(summary = "Delete Product Rating",
            description = "Delete a product rating by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted product rating", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ratingId) {
        productRatingService.delete(ratingId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_DELETE_RATING, null);
    }

    @Operation(summary = "Get All Product Ratings",
            description = "Retrieve all product ratings with pagination and filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all product ratings", content = @Content(schema = @Schema(implementation = CommonResponseProductRatingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
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
