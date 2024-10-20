package org.enigma.tokonyadia_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.request.UpdateProductRequest;
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
@RequestMapping(Constant.PRODUCT_API)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productServiceImpl;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @RequestPart(name = "menu") String product
    ) {
        try {
            ProductRequest productRequest = objectMapper.readValue(product, ProductRequest.class);
            ProductResponse productResponse = productServiceImpl.create(multipartFiles, productRequest);
            return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_PRODUCT, productResponse);
        } catch (Exception e) {
            return ResponseUtil.buildCommonResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        ProductResponse byId = productServiceImpl.getById(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT, byId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody UpdateProductRequest product) {
        ProductResponse productResponse = productServiceImpl.update(productId, product);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT, productResponse);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        productServiceImpl.delete(productId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT, null);
    }

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
