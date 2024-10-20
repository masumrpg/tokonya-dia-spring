package org.enigma.tokonyadia_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategoryRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 25, message = "Category name need min 3 char and max 25 char")
    private String name;

    @NotBlank(message = "Description is required")
    @Pattern(regexp = "^(\\b\\w+\\b\\W*){3,}$", message = "Description must contain at least 3 words")
    private String description;
}
