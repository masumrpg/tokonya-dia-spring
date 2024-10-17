package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPromoResponse {
    private String id;
    private String promoId;
    private String promoCode;
    private Double discount;
    private String startDateTime;
    private String endDateTime;
}
