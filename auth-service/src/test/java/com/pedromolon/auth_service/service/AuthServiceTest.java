package com.pedromolon.auth_service.service;

import com.pedromolon.auth_service.domain.Role;
import com.pedromolon.auth_service.domain.User;
import com.pedromolon.auth_service.dto.request.LoginRequest;
import com.pedromolon.auth_service.dto.request.RegisterRequest;
import com.pedromolon.auth_service.dto.response.LoginResponse;
import com.pedromolon.auth_service.dto.response.RegisterResponse;
import com.pedromolon.auth_service.exception.EmailAlreadyExistsException;
import com.pedromolon.auth_service.exception.UnauthorizedException;
import com.pedromolon.auth_service.repository.RoleRepository;
import com.pedromolon.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role(1L, "ROLE_USER");
        user = new User(1L, "Test User", "test@email.com", "encodedPassword", Set.of(userRole));
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("Test User", "test@email.com", "password123");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(user);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("Test User", response.name());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterExistingEmailThrowsException() {
        RegisterRequest request = new RegisterRequest("Test User", "test@email.com", "password123");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("test@email.com", "password123");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
    }

    @Test
    void testLoginInvalidCredentialsThrowsException() {
        LoginRequest request = new LoginRequest("test@email.com", "wrongpassword");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }
}
