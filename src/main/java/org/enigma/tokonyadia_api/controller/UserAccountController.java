package org.enigma.tokonyadia_api.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.dto.response.PersonResponse;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.utils.ResponseUtil;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.USER_API)
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final PersonService personService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        PersonResponse userResponse = personService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_USER, userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getSelfInfo() {
        UserResponse userResponse = userAccountService.getAuthentication();
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_FETCH_USER_INFO, userResponse);
    }
}