package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.request.UserUpdatePasswordRequest;
import org.enigma.tokonyadia_api.dto.response.CommonResponse;
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
@Tag(name = "User Account", description = "APIs for managing user accounts")
public class UserAccountController {
    private final UserAccountService userAccountService;


    @Operation(summary = "Create User", description = "Create a new user account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userAccountService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER, userResponse);
    }


    @Operation(summary = "Get Self Info", description = "Retrieve the authenticated user's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched user information", content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getSelfInfo() {
        UserResponse userResponse = userAccountService.getAuthentication();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_USER_INFO, userResponse);
    }

    @Operation(summary = "Delete Self Account", description = "Soft delete the authenticated user's account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account deleted successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelf() {
        userAccountService.softDelete();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_SELF, null);
    }

    @Operation(summary = "Reactivate Account", description = "Reactivate a soft deleted user account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account reactivated successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PostMapping("/reactivate")
    public ResponseEntity<?> reactivateAccount(@RequestBody UserRequest request) {
        userAccountService.reactiveSelf(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_REACTIVATE_ACCOUNT, null);
    }

    @Operation(summary = "Update User Password", description = "Allows a user to update their password by providing the current and new passwords. Admins can update any user's password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password updated successfully", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
            }
    )
    @PatchMapping("/me/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UserUpdatePasswordRequest request) {
        userAccountService.updatePassword(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PASSWORD, null);
    }

}