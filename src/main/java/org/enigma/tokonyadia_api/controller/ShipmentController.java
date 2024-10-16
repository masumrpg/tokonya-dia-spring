package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.ShipmentResponse;
import org.enigma.tokonyadia_api.service.ShipmentService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.SHIPMENT_API)
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;


    @GetMapping("/{shipmentId}")
    public ResponseEntity<?> getShipmentById(@PathVariable String shipmentId) {
        ShipmentResponse shipmentResponse = shipmentService.getById(shipmentId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_SHIPMENT, shipmentResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllShipments(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchCommonRequest searchCommonRequest = SearchCommonRequest.builder()
                .size(size)
                .page(page)
                .sortBy(sort)
                .query(query)
                .build();
        Page<ShipmentResponse> shipmentResponsePage = shipmentService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_SHIPMENT, shipmentResponsePage);
    }
}
