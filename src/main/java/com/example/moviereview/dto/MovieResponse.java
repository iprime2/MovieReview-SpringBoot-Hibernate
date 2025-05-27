package com.example.moviereview.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MovieResponse {
    private UUID id;
    private String title;
    private String description;
    private String director;
    private String genre;
    private Integer year;
    private LocalDateTime createdAt;
    private List<MovieImageResponse> images;
    private List<ReviewSummaryResponse> reviews;
}
