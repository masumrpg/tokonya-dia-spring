package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;

public interface UserService {
    UserResponse create(UserRequest request);
    UserAccount create(UserAccount userAccount);
}