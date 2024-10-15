package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String name;
    private String midtransCode;


}
