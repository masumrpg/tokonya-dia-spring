package org.enigma.tokonyadia_api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCategoryResponse {
    private String id;
    private String name;
    private String description;
}
