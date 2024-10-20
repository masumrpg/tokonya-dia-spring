package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDetailResponse {
    private String productId;
    private String productName;
    private Long price;
    private Integer quantity;
}
