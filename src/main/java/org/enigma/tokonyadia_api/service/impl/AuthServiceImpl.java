package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.AuthResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.AuthService;
import org.enigma.tokonyadia_api.service.JwtService;
import org.enigma.tokonyadia_api.service.RefreshTokenService;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAccountService userService;
    private final RefreshTokenService refreshTokenService;
    private final ValidationUtil validationUtil;

    @Override
    public AuthResponse login(AuthRequest request) {
        validationUtil.validate(request);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        String refreshToken = refreshTokenService.createToken(userAccount.getId());
        String accessToken = jwtService.generateAccessToken(userAccount);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Override
    public AuthResponse refreshToken(String token) {
        String userId = refreshTokenService.getUserIdByToken(token);
        UserAccount userAccount = userService.getById(userId);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(userId);
        String newToken = jwtService.generateAccessToken(userAccount);
        return AuthResponse.builder()
                .accessToken(newToken)
                .refreshToken(newRefreshToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        refreshTokenService.deleteRefreshToken(userAccount.getId());
        jwtService.blacklistAccessToken(accessToken);
    }
}
