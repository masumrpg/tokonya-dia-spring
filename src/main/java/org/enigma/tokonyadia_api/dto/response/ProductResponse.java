package org.enigma.tokonyadia_api.dto.response;

import lombok.*;
import org.enigma.tokonyadia_api.entity.Store;

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
    private Store store;
}
