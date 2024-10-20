package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderByStoreResponse {
    private String orderDetailId;
    private String storeId;
    private String storeName;
    private List<ProductDetailResponse> products;
    private ShipmentResponse shipment;
}
