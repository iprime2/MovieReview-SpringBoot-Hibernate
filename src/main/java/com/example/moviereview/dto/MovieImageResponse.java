package com.example.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for exposing image information related to a movie.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieImageResponse {
    private UUID id;
    private String name;
    private String imageUrl;
}
