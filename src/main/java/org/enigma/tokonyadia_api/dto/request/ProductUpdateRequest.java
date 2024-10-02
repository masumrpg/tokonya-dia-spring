package org.enigma.tokonyadia_api.dto.request;

import lombok.*;
import org.enigma.tokonyadia_api.entity.Store;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private String description;
    private Long price;
    private Integer stock;
    private String storeId;
}
