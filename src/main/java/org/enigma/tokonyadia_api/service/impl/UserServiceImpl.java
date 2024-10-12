package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.enigma.tokonyadia_api.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.enigma.tokonyadia_api.utils.MapperUtil.toUserResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {
        checkUsername(request.getUsername());
        try {
            UserRole userRole = UserRole.findByDescription(request.getRole());
            if (userRole == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ROLE_NOT_FOUND);
            UserAccount userAccount = UserAccount.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(userRole)
                    .build();
            userAccountRepository.saveAndFlush(userAccount);
            return toUserResponse(userAccount);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_USERNAME_DUPLICATE);
        }
    }

    @Override
    public UserAccount create(UserAccount userAccount) {
        checkUsername(userAccount.getUsername());
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        return userAccountRepository.saveAndFlush(userAccount);
    }

    @Override
    public UserAccount getById(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_USERNAME_NOT_FOUND));
    }

    @Override
    public UserResponse getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return toUserResponse(userAccount);
    }

    private void checkUsername(String username) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByUsername(username);
        if (optionalUserAccount.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_USERNAME_DUPLICATE);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
