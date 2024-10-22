package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRatingResponse {
    private String id;
    private String productId;
    private String personId;
    private Double rating;
    private String review;
    // TODO add auditInfo
}