package org.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Long price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be at least 0")
    private Integer stock;

    @NotBlank(message = "Store ID is required")
    private String storeId;

    @NotBlank(message = "Category ID is required")
    private String categoryId;
}
