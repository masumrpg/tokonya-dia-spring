package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.AuthResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.JwtService;
import org.enigma.tokonyadia_api.service.RefreshTokenService;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserAccountService userService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest("user12345", "Adminalsdwkkadkjaskdjkasd");
        UserAccount userAccount = new UserAccount();
        userAccount.setId("1");
        userAccount.setRole(UserRole.ROLE_ADMIN);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount, null);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(refreshTokenService.createToken(userAccount.getId())).thenReturn("refreshToken");
        Mockito.when(jwtService.generateAccessToken(userAccount)).thenReturn("accessToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Admin", response.getRole());
        Mockito.verify(validationUtil).validate(request);
    }

    @Test
    void testRefreshToken_Success() {
        String token = "refreshToken";
        UserAccount userAccount = new UserAccount();
        userAccount.setId("1");
        userAccount.setRole(UserRole.ROLE_ADMIN);

        Mockito.when(refreshTokenService.getUserIdByToken(token)).thenReturn("1");
        Mockito.when(userService.getById("1")).thenReturn(userAccount);
        Mockito.when(refreshTokenService.rotateRefreshToken("1")).thenReturn("newRefreshToken");
        Mockito.when(jwtService.generateAccessToken(userAccount)).thenReturn("newAccessToken");

        AuthResponse response = authService.refreshToken(token);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
        assertEquals("Admin", response.getRole());
    }

    @Test
    void testLogout_Success() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId("1");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = "accessToken";

        Mockito.doNothing().when(refreshTokenService).deleteRefreshToken(userAccount.getId());
        Mockito.doNothing().when(jwtService).blacklistAccessToken(accessToken);

        authService.logout(accessToken);

        Mockito.verify(refreshTokenService).deleteRefreshToken(userAccount.getId());
        Mockito.verify(jwtService).blacklistAccessToken(accessToken);
    }
}