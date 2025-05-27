package com.example.moviereview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequest {
    @NotNull
    private UUID movieId;

    @NotNull
    private UUID userId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer rating;

    @NotBlank
    private String comment;
}
