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
    private String customerName;
    private String customerPhoneNumber;
    private String storeName;
    private LocalDateTime transactionDate;
    private List<OrderDetailResponse> transactionsDetails;
}
