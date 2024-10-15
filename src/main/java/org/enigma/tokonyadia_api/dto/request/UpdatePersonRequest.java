package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePersonRequest {
    private String name;
    private String imgUrl;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
}
