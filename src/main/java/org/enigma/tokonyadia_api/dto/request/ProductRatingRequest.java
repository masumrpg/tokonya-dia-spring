package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRatingRequest {
    private String productId;
    private String personId;
    private Double rating;
    private String review;
}
