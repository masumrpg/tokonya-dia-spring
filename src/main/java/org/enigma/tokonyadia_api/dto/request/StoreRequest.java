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
public class StoreRequest {
    @NotBlank(message = "Store name is required")
    @Size(min = 3, max = 50, message = "Store name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Person ID is required")
    private String personId;

    @NotBlank(message = "SIUP is required")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "SIUP must contain only alphanumeric characters and hyphens")
    private String siup;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;
}
