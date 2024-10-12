package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.LoginResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.AuthService;
import org.enigma.tokonyadia_api.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userAccount);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }
}
