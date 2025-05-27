package com.example.moviereview.controller;

import com.example.moviereview.dto.PermissionRequest;
import com.example.moviereview.dto.PermissionResponse;
import com.example.moviereview.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW')")
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable UUID id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_VIEW')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable UUID id,
                                                               @Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.updatePermission(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Map<String, Object>> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        // send proper response on permission deletion
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Permission deleted successfully",
                        "id", id
                )
        );
    }
}
