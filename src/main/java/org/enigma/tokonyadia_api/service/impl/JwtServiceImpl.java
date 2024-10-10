package org.enigma.tokonyadia_api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateAccessToken(UserAccount userAccount) {
        log.info("Generating Jwt Token User: {}", userAccount.toString());
        try {
            Algorithm algorithm = Algorithm.HMAC256("secreat".getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer("Warung Makan Bahari")
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                    .withClaim("role", userAccount.getRole().getDescription())
                    .sign(algorithm); // header
        } catch (JWTCreationException e) {
            log.error("Error Creating Jwt Token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error JWT");
        }
    }
}
