package com.pedromolon.auth_service.service;

import com.pedromolon.auth_service.domain.Role;
import com.pedromolon.auth_service.domain.User;
import com.pedromolon.auth_service.dto.request.LoginRequest;
import com.pedromolon.auth_service.dto.request.RegisterRequest;
import com.pedromolon.auth_service.dto.response.LoginResponse;
import com.pedromolon.auth_service.dto.response.RegisterResponse;
import com.pedromolon.auth_service.exception.EmailAlreadyExistsException;
import com.pedromolon.auth_service.exception.ResourceNotFoundException;
import com.pedromolon.auth_service.exception.UnauthorizedException;
import com.pedromolon.auth_service.repository.RoleRepository;
import com.pedromolon.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getName());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = tokenService.generateToken(user);
        return new LoginResponse(token, 3600L);
    }

}
