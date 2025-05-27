package com.example.moviereview.service;

import com.example.moviereview.domain.entity.Permission;
import com.example.moviereview.domain.entity.Role;
import com.example.moviereview.domain.repository.PermissionRepository;
import com.example.moviereview.domain.repository.RoleRepository;
import com.example.moviereview.dto.RoleRequest;
import com.example.moviereview.dto.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // Create a new role
    public RoleResponse createRole(RoleRequest request) {
        log.info("[POST /api/roles] Creating new role: {} with permissions: {}", request.getName(), request.getPermissionNames());

        // Fetch permissions by names
        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionNames() != null) {
            permissions = request.getPermissionNames().stream()
                    .map(name -> permissionRepository.findByName(name)
                            .orElseThrow(() -> new NoSuchElementException("Permission not found: " + name)))
                    .collect(Collectors.toSet());
        }

        Role role = Role.builder()
                .name(request.getName())
                .permissions(permissions)
                .build();

        Role saved = roleRepository.save(role);
        return mapToResponse(saved);
    }

    // Get a role by ID
    public RoleResponse getRoleById(UUID id) {
        log.info("[GET /api/roles/{}] Fetching role", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + id));
        return mapToResponse(role);
    }

    // Get all roles
    public List<RoleResponse> getAllRoles() {
        log.info("[GET /api/roles] Fetching all roles");
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update only the role name
    public RoleResponse updateRole(UUID id, RoleRequest request) {
        log.info("[PUT /api/roles/{}] Updating role name", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + id));
        role.setName(request.getName());
        Role updated = roleRepository.save(role);
        return mapToResponse(updated);
    }

    // Delete role
    public void deleteRole(UUID id) {
        log.info("[DELETE /api/roles/{}] Deleting role", id);
        if (!roleRepository.existsById(id)) {
            log.warn("[DELETE /api/roles/{}] Role not found", id);
            throw new NoSuchElementException("Role not found: " + id);
        }
        roleRepository.deleteById(id);
        log.info("[DELETE /api/roles/{}] Deleted role", id);
    }

    /**
     * Link multiple permissions to a role at once.
     * @param roleId The role to link permissions to.
     * @param permissionIds The set of permission IDs to link.
     * @return RoleResponse with updated permissions.
     */
    public RoleResponse addPermissionsToRole(UUID roleId, Set<UUID> permissionIds) {
        log.info("[POST /api/roles/{}/permissions] Linking permissions {} to role", roleId, permissionIds);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + roleId));

        // Fetch all permissions by ID
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new NoSuchElementException("Some permissions not found for IDs: " + permissionIds);
        }

        // Add them to the role's permissions set
        role.getPermissions().addAll(permissions);
        Role updated = roleRepository.save(role);
        return mapToResponse(updated);
    }

    // Unlink permission from a role
    public RoleResponse removePermissionFromRole(UUID roleId, UUID permissionId) {
        log.info("[DELETE /api/roles/{}/permissions/{}] Unlinking permission from role", roleId, permissionId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new NoSuchElementException("Permission not found: " + permissionId));
        role.getPermissions().remove(permission);
        Role updated = roleRepository.save(role);
        return mapToResponse(updated);
    }

    // Maps Role entity to RoleResponse DTO
    private RoleResponse mapToResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions() == null
                        ? Set.of()
                        : role.getPermissions().stream()
                        .map(Permission::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
