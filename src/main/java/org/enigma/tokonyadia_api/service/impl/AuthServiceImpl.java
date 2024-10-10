package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.LoginResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.enigma.tokonyadia_api.service.AuthService;
import org.enigma.tokonyadia_api.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    @Override
    public LoginResponse login(AuthRequest request) {
        // TODO 1: Validasi user apakah username ada di db
        UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.INVALID_CREDENTIALS)
        );
        // TODO 2: Validasi password sesuai
        if (!passwordEncoder.matches(request.getPassword(), userAccount.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, Constant.INVALID_CREDENTIALS);
        // TODO 3: Generate token jika validasi berhasil
        String accessToken = jwtService.generateAccessToken(userAccount);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .role(userAccount.getRole().getDescription())
                .build();
    }
}
