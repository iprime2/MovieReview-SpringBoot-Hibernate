package com.example.moviereview.controller;

import com.example.moviereview.dto.RolePermissionLinkRequest;
import com.example.moviereview.dto.RoleRequest;
import com.example.moviereview.dto.RoleResponse;
import com.example.moviereview.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // Create role
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    // Get a role by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    // Get all roles
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Update role name
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID id, @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    // Delete role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Role deleted successfully",
                "id", id
        ));
    }

    // Add permission to a role
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> linkPermissionsToRole(
            @PathVariable UUID roleId,
            @RequestBody @Valid RolePermissionLinkRequest request) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, request.getPermissionIds()));
    }

    // Remove permission from a role
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<RoleResponse> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {
        return ResponseEntity.ok(roleService.removePermissionFromRole(roleId, permissionId));
    }
}
