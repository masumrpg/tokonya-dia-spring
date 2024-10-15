package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;
    private String imgUrl;
    private String categoryId;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
}
