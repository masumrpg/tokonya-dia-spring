package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderResponse {
    private String orderId;
    private String personId;
    private String personName;
    private String personPhoneNumber;
    private String orderDate;
    private String orderStatus;
    private List<OrderByStoreResponse> orderDetails;
}
