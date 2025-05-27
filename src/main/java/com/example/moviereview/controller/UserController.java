package com.example.moviereview.controller;

import com.example.moviereview.dto.UserRequest;
import com.example.moviereview.dto.UserResponse;
import com.example.moviereview.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST API Controller for managing users.
 * All modifying endpoints are protected by permission-based access.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     * Requires 'USER_CREATE' permission.
     */
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("[POST /api/users] Creating user for email: {}", request.getEmail());
        return ResponseEntity.ok(userService.createUser(request));
    }

    /**
     * Get all users.
     * Requires 'USER_VIEW' permission.
     */
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("[GET /api/users] Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get user by ID.
     * Requires 'USER_VIEW' permission.
     */
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        log.info("[GET /api/users/{}] Fetching user by ID", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Get user by email.
     * Requires 'USER_VIEW' permission.
     */
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("[GET /api/users/email/{}] Fetching user by email", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    /**
     * Delete a user by ID.
     * Requires 'USER_DELETE' permission.
     */
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable UUID id) {
        log.info("[DELETE /api/users/{}] Deleting user", id);
        userService.deleteUser(id);
        // sending proper response on permission deletion
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "User deleted successfully",
                        "id", id
                )
        );
    }
}
