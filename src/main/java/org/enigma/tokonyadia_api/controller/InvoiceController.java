package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
import org.enigma.tokonyadia_api.dto.response.InvoiceResponse;
import org.enigma.tokonyadia_api.service.InvoiceService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = Constant.INVOICE_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Invoice", description = "APIs for managing invoice, get invoice by ID")
public class InvoiceController {
    private static class CommonResponseInvoiceResponse extends CommonResponse<InvoiceResponse> {
    }

    private final InvoiceService invoiceService;

    @Operation(summary = "Get invoice by id",
            description = "This endpoint get invoice details by id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment created successfully", content = @Content(schema = @Schema(implementation = CommonResponseInvoiceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access - invalid signature", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            })
    @GetMapping("/{invoiceId}")
    public ResponseEntity<?> getInvoiceById(@PathVariable String invoiceId) {
        InvoiceResponse invoiceResponse = invoiceService.findById(invoiceId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_INVOICE, invoiceResponse);
    }

}
