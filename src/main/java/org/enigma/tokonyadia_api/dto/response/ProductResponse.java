package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
    private String productPromoId;
}
