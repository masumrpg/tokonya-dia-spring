package org.enigma.tokonyadia_api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;
import org.enigma.tokonyadia_api.constant.Constant;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${warung.makan.bahari.jwt-secret}")
    private String SECRET_KEY;

    @Value("${warung.makan.bahari.jwt-expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${warung.makan.bahari.jwt-issuer}")
    private String ISSUER;


    @Override
    public String generateAccessToken(UserAccount userAccount) {
        log.info("Generating JWT Token for User: {}", userAccount.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES))
                    .withSubject(userAccount.getId())
                    .withClaim("role", userAccount.getRole().getDescription())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Error Creating JWT Token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.ERROR_CREATE_JWT);
        }
    }

    @Override
    public String getUserId(String token) {
        log.info("Extract Token JWT - {}", System.currentTimeMillis());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            log.error("Error while validate JWT Token: {}", exception.getMessage());
            return null;
        }
    }
}

