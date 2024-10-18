package org.enigma.tokonyadia_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransNotificationRequest {
    @JsonProperty(value = "order_id")
    private String orderId;
    @JsonProperty(value = "status_code")
    private String statusCode;
    @JsonProperty(value = "gross_amount")
    private String grossAmount;
    @JsonProperty(value = "signature_key")
    private String signatureKey;
    @JsonProperty(value = "transaction_status")
    private String transactionStatus;
    @JsonProperty(value = "transaction_time")
    private String transactionTime;
}