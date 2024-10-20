package org.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.enigma.tokonyadia_api.constant.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}