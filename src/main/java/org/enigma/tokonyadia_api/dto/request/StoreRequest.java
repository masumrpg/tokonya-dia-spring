package org.enigma.tokonyadia_api.dto.request;


import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {
    private String siup;
    private String name;
    private String phoneNumber;
    private String address;
}
