package org.enigma.tokonyadia_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userAccountService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER, userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getSelfInfo() {
        UserResponse userResponse = userAccountService.getAuthentication();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_USER_INFO, userResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelf() {
        userAccountService.softDelete();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_SELF, null);
    }

    @PostMapping("/reactivate")
    public ResponseEntity<?> reactivateAccount(@RequestBody UserRequest request) {
        userAccountService.reactiveSelf(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_REACTIVATE_ACCOUNT, null);
    }
}