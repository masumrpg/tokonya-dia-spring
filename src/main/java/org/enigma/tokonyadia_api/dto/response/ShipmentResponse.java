package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentResponse {
    private String shipmentId;
    private String orderId;
    private String courierName;
    private String receipt;
    private String deliveryDate;
    private String deliveryFrom;
    private String deliveryTo;
    private String estimateDate;
    private String status;
}
