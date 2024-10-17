package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.ProductPromoRequest;
import org.enigma.tokonyadia_api.dto.request.SearchWithMinMaxRequest;
import org.enigma.tokonyadia_api.dto.response.ProductPromoResponse;
import org.enigma.tokonyadia_api.service.ProductPromoService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.PRODUCT_PROMO_API)
@RequiredArgsConstructor
public class ProductPromoController {
    private final ProductPromoService productPromoService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<?> createPromo(@RequestBody ProductPromoRequest request) {
        ProductPromoResponse productPromoResponse = productPromoService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_PROMO, productPromoResponse);
    }

    @GetMapping("/{promoId}")
    public ResponseEntity<?> getPromo(@PathVariable String promoId) {
        ProductPromoResponse productPromoResponse = productPromoService.getById(promoId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PRODUCT_PROMO, productPromoResponse);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{promoId}")
    public ResponseEntity<?> updatePromo(@PathVariable String promoId, @RequestBody ProductPromoRequest request) {
        ProductPromoResponse productPromoResponse = productPromoService.update(promoId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PRODUCT_PROMO, productPromoResponse);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @DeleteMapping("/{promoId}")
    public ResponseEntity<?> deletePromo(@PathVariable String promoId) {
        productPromoService.delete(promoId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PRODUCT_PROMO, null);
    }

    @GetMapping
    public ResponseEntity<?> getAllPromos(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "minValue", required = false) Long minValue,
            @RequestParam(name = "maxValue", required = false) Long maxValue
    ) {
        SearchWithMinMaxRequest searchWithMinMaxRequest = SearchWithMinMaxRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .sortBy(sort)
                .minValue(minValue)
                .maxValue(maxValue)
                .build();
        Page<ProductPromoResponse> productPromoResponsePage = productPromoService.getAll(searchWithMinMaxRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PRODUCT_PROMO, productPromoResponsePage);
    }
}
