package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(AuthRequest request);
}
