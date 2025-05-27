package com.example.moviereview.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewSummaryResponse {
    private UUID id;
    private Integer rating;
    private String comment;
    private UUID userId;
    private String userFullName;
}
