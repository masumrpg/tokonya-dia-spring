package org.enigma.tokonyadia_api.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.request.UserUpdatePasswordRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;

    // =================== INIT ADMIN ACCOUNT ===========================

    @Value("${tokonya.dia.user-admin}")
    private String USERNAME_ADMIN;

    @Value("${tokonya.dia.user-password}")
    private String PASSWORD_ADMIN;


    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initUser() {
        boolean exist = userAccountRepository.existsByUsername(USERNAME_ADMIN);
        if (exist) return;
        UserAccount userAccount = UserAccount.builder()
                .username(USERNAME_ADMIN)
                .password(passwordEncoder.encode(PASSWORD_ADMIN))
                .role(UserRole.ROLE_ADMIN)
                .build();

        userAccountRepository.saveAndFlush(userAccount);
    }

    // ===================================================================


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(UserRequest request) {
        validationUtil.validate(request);

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_ADMIN)
                .build();
        userAccountRepository.saveAndFlush(userAccount);
        return MapperUtil.toUserResponse(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount create(UserAccount userAccount) {
        validationUtil.validate(userAccount);
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        return userAccountRepository.saveAndFlush(userAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getById(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_USERNAME_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return MapperUtil.toUserResponse(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(UserUpdatePasswordRequest request) {
        validationUtil.validate(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount account = (UserAccount) authentication.getPrincipal();
        UserAccount userAccount = getById(account.getId());

        if (!passwordEncoder.matches(request.getCurrentPassword(), userAccount.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constant.INVALID_CREDENTIALS);
        }

        userAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.saveAndFlush(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void softDelete() {
        UserAccount currentUser = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setIsDeleted(true);
        userAccountRepository.save(currentUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reactiveSelf(UserRequest request) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByUsername(request.getUsername());
        if (optionalUserAccount.isEmpty() || optionalUserAccount.get().getIsDeleted().equals(false))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_USERNAME_NOT_FOUND);
        UserAccount userAccount = optionalUserAccount.get();
        userAccount.setIsDeleted(false);
        userAccountRepository.save(userAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean existsByUsernameAndIsDeletedTrue = userAccountRepository.existsByUsernameAndIsDeletedTrue(username);
        if (existsByUsernameAndIsDeletedTrue) {
            throw new DisabledException("Account is deactivated!");
        }
        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Constant.ERROR_USERNAME_NOT_FOUND));
    }
}
