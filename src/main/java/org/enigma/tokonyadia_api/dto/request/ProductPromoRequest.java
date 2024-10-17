package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPromoRequest {
    private String productId;
    private String promoCode;
    private Double discount;
    private String startDateTime;
    private String endDateTime;
}
