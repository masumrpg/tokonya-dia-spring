package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceItemResponse {
    private String itemId;
    private String productName;
    private Integer quantity;
    private Long price;
    private Double totalPrice;
}
