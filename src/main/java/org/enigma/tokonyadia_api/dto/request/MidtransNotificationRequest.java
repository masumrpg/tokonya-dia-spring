package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransNotificationRequest {
    //    @NotBlank(message = "Order id is required")
    private String orderId;
}
