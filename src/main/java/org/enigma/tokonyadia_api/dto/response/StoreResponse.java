package org.enigma.tokonyadia_api.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    private String id;
    private String name;
    private String personId;
    private String siup;
    private String phoneNumber;
    private String address;
}
