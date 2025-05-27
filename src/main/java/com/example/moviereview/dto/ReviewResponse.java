package com.example.moviereview.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReviewResponse {
    private UUID id;
    private Integer rating;
    private String comment;
    private UUID movieId;
    private String movieTitle;
    private UUID userId;
    private String userFullName;
    private LocalDateTime createdAt;
}
