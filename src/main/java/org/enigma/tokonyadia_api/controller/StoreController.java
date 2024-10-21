package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.STORE_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Store Management", description = "APIs for managing stores")
public class StoreController {
    private final StoreService storeService;


    @Operation(summary = "Create Store", description = "Create a new store")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.create(storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_STORE, storeResponse);
    }

    @Operation(summary = "Get Store by ID", description = "Retrieve a store by its ID")
    @GetMapping(path = "/{storeId}")
    public ResponseEntity<?> getProductById(@PathVariable String storeId) {
        StoreResponse storeResponse = storeService.getById(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_STORE, storeResponse);
    }

    @Operation(summary = "Update Store", description = "Update an existing store by its ID")
    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateProduct(@PathVariable String storeId, @RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.update(storeId, storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_STORE, storeResponse);
    }

    @Operation(summary = "Delete Store", description = "Delete a store by its ID")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String storeId) {
        storeService.delete(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_STORE, null);
    }

    @Operation(summary = "Get All Stores", description = "Retrieve all stores with pagination and filtering")
    @GetMapping
    public ResponseEntity<?> getAllStores(
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
        Page<StoreResponse> storeResponsePage = storeService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_All_STORE, storeResponsePage);
    }
}
