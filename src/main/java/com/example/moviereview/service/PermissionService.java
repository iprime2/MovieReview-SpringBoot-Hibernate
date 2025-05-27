package com.example.moviereview.service;

import com.example.moviereview.domain.entity.Permission;
import com.example.moviereview.domain.repository.PermissionRepository;
import com.example.moviereview.dto.PermissionRequest;
import com.example.moviereview.dto.PermissionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    /**
     * Create a new permission.
     * Route: POST /api/permissions
     */
    public PermissionResponse createPermission(PermissionRequest request) {
        log.info("[POST /api/permissions] Request to create permission: {}", request.getName());
        Permission permission = Permission.builder().name(request.getName()).build();
        Permission saved = permissionRepository.save(permission);
        log.info("[POST /api/permissions] Created permission with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    /**
     * Fetch a permission by its id.
     * Route: GET /api/permissions/{id}
     */
    public PermissionResponse getPermissionById(UUID id) {
        log.info("[GET /api/permissions/{}] Fetching permission", id);
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[GET /api/permissions/{}] Permission not found", id);
                    return new NoSuchElementException("Permission not found: " + id);
                });
        return mapToResponse(permission);
    }

    /**
     * List all permissions.
     * Route: GET /api/permissions
     */
    public List<PermissionResponse> getAllPermissions() {
        log.info("[GET /api/permissions] Fetching all permissions");
        List<PermissionResponse> permissions = permissionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        log.info("[GET /api/permissions] Found {} permissions", permissions.size());
        return permissions;
    }

    /**
     * Update a permission by id.
     * Route: PUT /api/permissions/{id}
     */
    public PermissionResponse updatePermission(UUID id, PermissionRequest request) {
        log.info("[PUT /api/permissions/{}] Updating permission", id);
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[PUT /api/permissions/{}] Permission not found", id);
                    return new NoSuchElementException("Permission not found: " + id);
                });
        permission.setName(request.getName());
        Permission updated = permissionRepository.save(permission);
        log.info("[PUT /api/permissions/{}] Updated permission", updated.getId());
        return mapToResponse(updated);
    }

    /**
     * Delete a permission by id.
     * Route: DELETE /api/permissions/{id}
     */
    public void deletePermission(UUID id) {
        log.info("[DELETE /api/permissions/{}] Deleting permission", id);
        if (!permissionRepository.existsById(id)) {
            log.warn("[DELETE /api/permissions/{}] Permission not found", id);
            throw new NoSuchElementException("Permission not found: " + id);
        }
        permissionRepository.deleteById(id);
        log.info("[DELETE /api/permissions/{}] Deleted permission", id);
    }

    // Map Permission entity to response DTO
    private PermissionResponse mapToResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }
}

