package org.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailRequest {
    @NotBlank(message = "Product id is required")
    private String productId;
}
