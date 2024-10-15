package org.enigma.tokonyadia_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonResponse {
    private String id;
    private String name;
    private String imageUrl;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String userAccountId;
}
