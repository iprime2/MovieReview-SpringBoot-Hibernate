package com.example.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleSummary {
    private UUID id;
    private String name;
}
