package com.example.moviereview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovieRequest {
    @NotBlank
    private String title;

    private String description;

    private String director;

    private String genre;

    @NotNull
    private Integer year;
}
