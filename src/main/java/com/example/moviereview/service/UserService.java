package com.example.moviereview.service;

import com.example.moviereview.domain.entity.Role;
import com.example.moviereview.domain.entity.User;
import com.example.moviereview.domain.repository.RoleRepository;
import com.example.moviereview.domain.repository.UserRepository;
import com.example.moviereview.dto.PermissionSummary;
import com.example.moviereview.dto.RoleSummary;
import com.example.moviereview.dto.UserRequest;
import com.example.moviereview.dto.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user from request payload.
     * @param request UserRequest DTO
     * @return UserResponse DTO
     */
    public UserResponse createUser(UserRequest request) {
        log.info("[POST /api/users] Creating user with email: {}", request.getEmail());

        Set<Role> roles = request.getRoleNames().stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new NoSuchElementException("Role not found: " + name)))
                .collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hashing the password using crypto
                .fullName(request.getFullName())
                .roles(roles)
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        log.info("[POST /api/users] Created user with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    public UserResponse getUserById(UUID id) {
        log.info("[GET /api/users/{id}] Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return mapToResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.info("[GET /api/users/email] Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        log.info("[GET /api/users] Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteUser(UUID id) {
        log.info("[DELETE /api/users/{id}] Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("[DELETE /api/users/{id}] Deleted user with id: {}", id);
    }

    /**
     * Maps User entity to UserResponse DTO.
     */
    private UserResponse mapToResponse(User user) {
        // Map roles
        Set<RoleSummary> roleSummaries = user.getRoles().stream()
                .map(role -> new RoleSummary(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        // Map permissions (flatten all permissions across all roles)
        Set<PermissionSummary> permissionSummaries = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new PermissionSummary(permission.getId(), permission.getName()))
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .enabled(user.getEnabled())
                .roles(roleSummaries)
                .permissions(permissionSummaries)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
