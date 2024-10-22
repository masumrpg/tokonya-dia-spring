package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.request.UserUpdatePasswordRequest;
import org.enigma.tokonyadia_api.dto.response.UserResponse;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private UserAccount userAccount;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userAccount = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("encodedpassword")
                .role(UserRole.ROLE_ADMIN)
                .isDeleted(false)
                .build();

        userRequest = UserRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        userResponse = UserResponse.builder()
                .id("1")
                .username("testuser")
                .role(UserRole.ROLE_ADMIN.getDescription())
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserAccount savedUserAccount = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("encodedpassword")
                .role(UserRole.ROLE_ADMIN)
                .isDeleted(false)
                .build();

        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(savedUserAccount);
        doNothing().when(validationUtil).validate(any(UserRequest.class));

        UserResponse result = userAccountService.create(userRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(result.getId(), result.getId());
        assertEquals(UserRole.ROLE_ADMIN.getDescription(), result.getRole());

        verify(validationUtil).validate(userRequest);
        verify(passwordEncoder).encode("password123");
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
    }

    @Test
    void shouldCreateUserAccountSuccessfully() {
        when(passwordEncoder.encode(any())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(userAccount);

        UserAccount result = userAccountService.create(userAccount);

        assertNotNull(result);
        assertEquals(userAccount.getUsername(), result.getUsername());
        verify(validationUtil).validate(userAccount);
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        when(userAccountRepository.findById("1")).thenReturn(Optional.of(userAccount));

        UserAccount result = userAccountService.getById("1");

        assertNotNull(result);
        assertEquals(userAccount.getId(), result.getId());
        verify(userAccountRepository).findById("1");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userAccountRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userAccountService.getById("999"));
        verify(userAccountRepository).findById("999");
    }

    @Test
    void shouldGetAuthenticationSuccessfully() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAccount);

        UserResponse result = userAccountService.getAuthentication();

        assertNotNull(result);
        assertEquals(userResponse.getUsername(), result.getUsername());
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        UserUpdatePasswordRequest updateRequest = UserUpdatePasswordRequest.builder()
                .currentPassword("currentPass")
                .newPassword("newPass")
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAccount);
        when(userAccountRepository.findById(userAccount.getId())).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.matches(updateRequest.getCurrentPassword(), userAccount.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(updateRequest.getNewPassword())).thenReturn("encodednewpassword");

        userAccountService.updatePassword(updateRequest);

        verify(validationUtil).validate(updateRequest);
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordInvalid() {
        UserUpdatePasswordRequest updateRequest = UserUpdatePasswordRequest.builder()
                .currentPassword("wrongPass")
                .newPassword("newPass")
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAccount);
        when(userAccountRepository.findById(userAccount.getId())).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.matches(updateRequest.getCurrentPassword(), userAccount.getPassword())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userAccountService.updatePassword(updateRequest));
    }

    @Test
    void shouldSoftDeleteSuccessfully() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAccount);

        userAccountService.softDelete();

        assertTrue(userAccount.getIsDeleted());
        verify(userAccountRepository).save(userAccount);
    }

    @Test
    void shouldReactiveSelfSuccessfully() {
        userAccount.setIsDeleted(true);
        when(userAccountRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(userAccount));

        userAccountService.reactiveSelf(userRequest);

        assertFalse(userAccount.getIsDeleted());
        verify(userAccountRepository).save(userAccount);
    }

    @Test
    void shouldThrowExceptionWhenReactivatingNonexistentUser() {
        when(userAccountRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userAccountService.reactiveSelf(userRequest));
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        when(userAccountRepository.existsByUsernameAndIsDeletedTrue("testuser")).thenReturn(false);
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(userAccount));

        UserAccount result = (UserAccount) userAccountService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(userAccount.getUsername(), result.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenLoadingDeactivatedUser() {
        when(userAccountRepository.existsByUsernameAndIsDeletedTrue("testuser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userAccountService.loadUserByUsername("testuser"));
    }
}