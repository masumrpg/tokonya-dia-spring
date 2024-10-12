package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponse create(UserRequest request);
    UserAccount create(UserAccount userAccount);
    UserAccount getById(String id);
    UserResponse getAuthentication();
}