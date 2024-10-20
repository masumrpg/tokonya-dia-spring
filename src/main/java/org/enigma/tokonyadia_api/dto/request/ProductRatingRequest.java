package org.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRatingRequest {
    @NotBlank(message = "Product id is required")
    private String productId;

    @NotBlank(message = "Person id is required")
    private String personId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Double rating;

    @NotBlank(message = "Review is required")
    private String review;
}
