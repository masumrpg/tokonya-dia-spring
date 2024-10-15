package org.enigma.tokonyadia_api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.enigma.tokonyadia_api.entity.UserAccount;

public interface JwtService {
    String generateAccessToken(UserAccount userAccount);

    String getUserId(String token);

    String extractTokenFromRequest(HttpServletRequest request);

    void blacklistAccessToken(String bearerToken);

    boolean isTokenBlacklisted(String token);
}