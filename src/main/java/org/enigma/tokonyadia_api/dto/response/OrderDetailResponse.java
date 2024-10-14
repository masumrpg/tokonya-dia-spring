package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDetailResponse {
    private String productId;
    private String productName;
    private String storeName;
    private Long price;
    private Integer quantity;
//    private String discount; // for future feature
}
