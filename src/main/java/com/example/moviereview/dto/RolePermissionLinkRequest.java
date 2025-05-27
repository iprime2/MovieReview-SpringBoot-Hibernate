package com.example.moviereview.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class RolePermissionLinkRequest {
    private Set<UUID> permissionIds;
}
