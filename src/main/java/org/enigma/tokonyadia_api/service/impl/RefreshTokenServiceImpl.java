package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.service.RedisService;
import org.enigma.tokonyadia_api.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${warung.makan.bahari.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    private final RedisService redisService;

    @Override
    public String createToken(String userId) {
        String refreshToken = UUID.randomUUID().toString();

        if (redisService.get("refreshToken:" + userId) != null) {
            deleteRefreshToken(userId);
        }

        redisService.save("refreshToken:" + userId, refreshToken, Duration.ofHours(REFRESH_TOKEN_EXPIRY));
        redisService.save("refreshTokenMap:" + refreshToken, userId, Duration.ofHours(REFRESH_TOKEN_EXPIRY));
        return refreshToken;
    }

    @Override
    public void deleteRefreshToken(String userId) {
        String token = redisService.get("refreshToken:" + userId);
        redisService.delete("refreshToken:" + userId);
        redisService.delete("refreshTokenMap:" + token);
    }

    @Override
    public String rotateRefreshToken(String userId) {
        deleteRefreshToken(userId);
        return createToken(userId);
    }

    @Override
    public String getUserIdByToken(String token) {
        return redisService.get("refreshTokenMap:" + token);
    }
}
