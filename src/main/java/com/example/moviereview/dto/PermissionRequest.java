package com.example.moviereview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for creating/updating permissions.
 */
@Data
public class PermissionRequest {
    @NotBlank(message = "Permission name must not be blank")
    private String name;
}
