package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Account Management", description = "APIs for managing user accounts")
public class UserAccountController {
    private final UserAccountService userAccountService;


    @Operation(summary = "Create User", description = "Create a new user account")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userAccountService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER, userResponse);
    }


    @Operation(summary = "Get Self Info", description = "Retrieve the authenticated user's information")
    @GetMapping("/me")
    public ResponseEntity<?> getSelfInfo() {
        UserResponse userResponse = userAccountService.getAuthentication();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_USER_INFO, userResponse);
    }
    
    @Operation(summary = "Delete Self Account", description = "Soft delete the authenticated user's account")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelf() {
        userAccountService.softDelete();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_SELF, null);
    }

    @Operation(summary = "Reactivate Account", description = "Reactivate a soft deleted user account")
    @PostMapping("/reactivate")
    public ResponseEntity<?> reactivateAccount(@RequestBody UserRequest request) {
        userAccountService.reactiveSelf(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_REACTIVATE_ACCOUNT, null);
    }
}