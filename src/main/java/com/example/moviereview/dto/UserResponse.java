package com.example.moviereview.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private boolean enabled;
    private Set<RoleSummary> roles;
    private Set<PermissionSummary> permissions;
    private LocalDateTime createdAt;
}
