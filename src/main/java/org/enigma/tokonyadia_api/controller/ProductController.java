package org.enigma.tokonyadia_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRequest;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
import org.enigma.tokonyadia_api.dto.response.ProductResponse;
import org.enigma.tokonyadia_api.service.ProductService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = Constant.PRODUCT_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product", description = "APIs for managing products")
public class ProductController {
    private static class CommonResponseProductResponse extends CommonResponse<ProductResponse> {
    }

    private final ProductService productServiceImpl;
    private final ObjectMapper objectMapper;


    @Operation(summary = "Create Product",
            description = "Create a new product with optional images",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created product", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(name = "images", required = false) List<MultipartFile> multipartFiles,
            @RequestPart(name = "product") String product
    ) {
        try {
            ProductRequest productRequest = objectMapper.readValue(product, ProductRequest.class);
            ProductResponse productResponse = productServiceImpl.create(multipartFiles, productRequest);
            return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_PRODUCT, productResponse);
        } catch (Exception e) {
            return ResponseUtil.buildCommonResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(summary = "Get Product by ID",
            description = "Retrieve a product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved product", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @GetMapping(path = "/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        ProductResponse productResponse = productServiceImpl.getById(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT, productResponse);
    }


    @Operation(summary = "Update Product",
            description = "Update an existing product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated product", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String productId,
            @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(name = "images", required = false) List<MultipartFile> files,
            @RequestPart(name = "product") String product
    ) {
        try {
            UpdateProductRequest updateProductRequest = objectMapper.readValue(product, UpdateProductRequest.class);
            ProductResponse productResponse = productServiceImpl.update(productId, files, updateProductRequest);
            return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT, productResponse);
        } catch (Exception e) {
            return ResponseUtil.buildCommonResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(summary = "Delete Product",
            description = "Delete a product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted product", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        productServiceImpl.deleteById(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT, null);
    }

    @Operation(summary = "Get All Products",
            description = "Retrieve all products with pagination and filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all products", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllProducts(
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
        Page<ProductResponse> productResponsePage = productServiceImpl.getAll(searchWithMinMaxRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT, productResponsePage);
    }
}
