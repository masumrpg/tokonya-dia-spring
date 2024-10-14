package org.enigma.tokonyadia_api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private String name;
    private String midtransCode;


}
