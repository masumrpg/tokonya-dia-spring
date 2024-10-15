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
    private String id;
    private String personName;
    private String personPhoneNumber;
    private String orderDate;
    private List<OrderDetailResponse> orderDetails;
}
