package org.enigma.tokonyadia_api.dto.response;

import lombok.*;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.TransactionDetail;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransactionDetailResponse {
    private String productId;
    private String productName;
    private Long price;
    private Integer quantity;
//    private String discount; // for future feature
}
