package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
}
