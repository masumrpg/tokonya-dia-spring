package org.enigma.tokonyadia_api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private UserAccount userAccount;

    @BeforeEach
    public void setUp() {
        userAccount = new UserAccount();
        userAccount.setId("12345");
        userAccount.setRole(UserRole.ROLE_ADMIN);
        System.setProperty("tokonya.dia.jwt-secret", "secret");
        System.setProperty("tokonya.dia.jwt-expiration-in-minutes-access-token", "10");
        System.setProperty("tokonya.dia.jwt-issuer", "testIssuer");
    }

    @Test
    void testGenerateAccessToken() {
        String token = jwtService.generateAccessToken(userAccount);

        assertNotNull(token);
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("testIssuer", decodedJWT.getIssuer());
        assertEquals("12345", decodedJWT.getSubject());
    }

    @Test
    void testGetUserId_ValidToken() {
        String token = jwtService.generateAccessToken(userAccount);

        String userId = jwtService.getUserId(token);

        assertEquals("12345", userId);
    }

    @Test
    void testGetUserId_InvalidToken() {
        String userId = jwtService.getUserId("invalidToken");

        assertNull(userId);
    }

    @Test
    void testExtractTokenFromRequest() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "Bearer some.jwt.token";
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);

        String extractedToken = jwtService.extractTokenFromRequest(request);

        assertEquals("some.jwt.token", extractedToken);
    }

    @Test
    void testBlacklistAccessToken() {
        String token = jwtService.generateAccessToken(userAccount);

        jwtService.blacklistAccessToken("Bearer " + token);

        Mockito.verify(redisService, Mockito.times(1)).save(any(), eq("BLACKLISTED"), any());
    }

    @Test
    void testIsTokenBlacklisted() {
        String token = jwtService.generateAccessToken(userAccount);
        Mockito.when(redisService.get(token)).thenReturn("BLACKLISTED");

        boolean isBlacklisted = jwtService.isTokenBlacklisted(token);

        assertTrue(isBlacklisted);
    }

    @Test
    void testIsTokenBlacklisted_NotFound() {
        String token = jwtService.generateAccessToken(userAccount);
        Mockito.when(redisService.get(token)).thenReturn(null);

        boolean isBlacklisted = jwtService.isTokenBlacklisted(token);

        assertFalse(isBlacklisted);
    }

    @Test
    void testBlacklistAccessToken_InvalidToken() {
        Mockito.when(redisService.get("invalidToken")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> jwtService.blacklistAccessToken("Bearer invalidToken"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Token invalid", exception.getReason());
    }
}