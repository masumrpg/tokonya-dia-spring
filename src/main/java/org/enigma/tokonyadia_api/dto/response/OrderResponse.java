package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime orderDate;
    private List<OrderDetailResponse> orderDetails;
}
