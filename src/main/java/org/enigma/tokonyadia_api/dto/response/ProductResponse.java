package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String imageUrl;
    private String category;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
    private List<ProductPromoResponse> productPromoId;
}
