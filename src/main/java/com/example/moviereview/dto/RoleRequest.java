package com.example.moviereview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class RoleRequest {
    @NotBlank
    private String name;

    private Set<String> permissionNames;
}
