package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private String id;

    private String orderId;

    private String invoiceCode;

    private String customerName;

    private Double totalAmount;

    private String shopName;

    private List<InvoiceItemResponse> items;
}
