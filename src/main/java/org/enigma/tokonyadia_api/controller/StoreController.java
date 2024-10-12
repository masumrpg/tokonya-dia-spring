package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.StoreRequest;
import org.enigma.tokonyadia_api.dto.response.StoreResponse;
import org.enigma.tokonyadia_api.service.StoreService;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.STORE_API)
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.create(storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_STORE, storeResponse);
    }

    @GetMapping(path = "/{storeId}")
    public ResponseEntity<?> getProductById(@PathVariable String storeId) {
        StoreResponse storeResponse = storeService.getById(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_STORE, storeResponse);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateProduct(@PathVariable String storeId, @RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.update(storeId, storeRequest);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_STORE, storeResponse);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String storeId) {
        storeService.delete(storeId);
        return ResponseUtil.buildCommonResponse(HttpStatus.NO_CONTENT, Constant.SUCCESS_DELETE_STORE, null);
    }

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
        Page<StoreResponse> customerResponsePage = storeService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_All_STORE, customerResponsePage);
    }
}
