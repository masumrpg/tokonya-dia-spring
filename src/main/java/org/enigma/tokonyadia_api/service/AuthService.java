package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse refreshToken(String token);

    void logout(String accessToken);

}
