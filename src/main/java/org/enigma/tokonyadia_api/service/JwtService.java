package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.entity.UserAccount;

public interface JwtService {
    String generateAccessToken(UserAccount userAccount);
}