package org.enigma.tokonyadia_api.dto.response;

import lombok.*;
import org.enigma.tokonyadia_api.constant.PaymentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String orderId;
    private Long amount;
    private PaymentStatus paymentStatus;
    private String tokenSnap;
    private String redirectUrl;
}
