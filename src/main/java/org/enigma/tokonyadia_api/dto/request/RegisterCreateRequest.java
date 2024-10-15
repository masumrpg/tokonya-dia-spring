package org.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCreateRequest {
    private String username;
    private String password;
    private String role;
    private String name;
    private String gender;
    private String email;
    private String phoneNumber;
}