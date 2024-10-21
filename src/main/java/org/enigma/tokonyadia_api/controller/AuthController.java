package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.AuthRequest;
import org.enigma.tokonyadia_api.dto.response.AuthResponse;
import org.enigma.tokonyadia_api.service.AuthService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;


@RestController
@RequestMapping(path = Constant.AUTH_API)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and token management")
public class AuthController {
    @Value("${tokonya.dia.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    private final AuthService authService;


    @Operation(summary = "User Login", description = "Authenticate a user and return access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_LOGIN, authResponse);
    }


    @Operation(summary = "Refresh Access Token", description = "Generate a new access token using the refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping(path = "/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GENERATED_ACCESS_TOKEN, authResponse);
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "User Logout", description = "Logout a user by invalidating their access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearerToken);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_LOGOUT, null);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(Constant.REFRESH_TOKEN_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.REFRESH_TOKEN_REQUIRED));
        return cookie.getValue();
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(Constant.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * REFRESH_TOKEN_EXPIRY);
        response.addCookie(cookie);
    }
}