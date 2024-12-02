package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductCategoryRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
import org.enigma.tokonyadia_api.dto.response.ProductCategoryResponse;
import org.enigma.tokonyadia_api.service.ProductCategoryService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.CATEGORY_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product Category", description = "APIs for managing product categories")
public class ProductCategoryController {
    private static class CommonResponseProductCategoryResponse extends CommonResponse<ProductCategoryResponse> {
    }

    private final ProductCategoryService productCategoryService;


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Product Category",
            description = "Create a new product category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created product category", content = @Content(schema = @Schema(implementation = CommonResponseProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> createProductCategory(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, "Successfully created product category", productCategoryResponse);
    }

    @Operation(summary = "Get Product Category",
            description = "Retrieve a product category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved product category", content = @Content(schema = @Schema(implementation = CommonResponseProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @GetMapping("/{productCategoryId}")
    public ResponseEntity<?> getProductCategory(@PathVariable String productCategoryId) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.getById(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully retrieved product category", productCategoryResponse);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Product Category",
            description = "Update an existing product category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated product category", content = @Content(schema = @Schema(implementation = CommonResponseProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PutMapping("/{productCategoryId}")
    public ResponseEntity<?> updateProductCategory(@PathVariable String productCategoryId, @RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse productCategoryResponse = productCategoryService.update(productCategoryId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully updated product category", productCategoryResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Product Category",
            description = "Delete a product category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted product category", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable String productCategoryId) {
        productCategoryService.delete(productCategoryId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, "Successfully deleted product category", null);
    }

    @Operation(summary = "Get All Product Categories",
            description = "Retrieve all product categories with pagination and filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all product categories", content = @Content(schema = @Schema(implementation = CommonResponseProductCategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
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
