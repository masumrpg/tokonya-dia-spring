package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.service.RedisService;
import org.enigma.tokonyadia_api.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${tokonya.dia.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    private final RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRefreshToken(String userId) {
        String token = redisService.get("refreshToken:" + userId);
        redisService.delete("refreshToken:" + userId);
        redisService.delete("refreshTokenMap:" + token);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String rotateRefreshToken(String userId) {
        deleteRefreshToken(userId);
        return createToken(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public String getUserIdByToken(String token) {
        return redisService.get("refreshTokenMap:" + token);
    }
}
