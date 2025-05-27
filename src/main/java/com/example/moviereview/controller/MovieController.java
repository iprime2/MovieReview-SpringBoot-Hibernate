package com.example.moviereview.controller;

import com.example.moviereview.dto.MovieRequest;
import com.example.moviereview.dto.MovieResponse;
import com.example.moviereview.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * MovieController exposes endpoints for movie CRUD operations.
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    /**
     * Create a new movie. Requires MOVIE_CREATE permission.
     */
    @PreAuthorize("hasAuthority('MOVIE_CREATE')")
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        log.info("[POST /api/movies] Creating movie");
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    /**
     * Get a movie by its ID.
     */
    @PreAuthorize("hasAuthority('MOVIE_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable UUID id) {
        log.info("[GET /api/movies/{}] Fetching movie", id);
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    /**
     * Get all movies.
     */
    @PreAuthorize("hasAuthority('MOVIE_VIEW')")
    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        log.info("[GET /api/movies] Fetching all movies");
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    /**
     * Update a movie.
     */
    @PreAuthorize("hasAuthority('MOVIE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable UUID id, @Valid @RequestBody MovieRequest request) {
        log.info("[PUT /api/movies/{}] Updating movie", id);
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    /**
     * Delete a movie.
     */
    @PreAuthorize("hasAuthority('MOVIE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        log.info("[DELETE /api/movies/{}] Deleting movie", id);
        movieService.deleteMovie(id);
        return ResponseEntity.ok(Map.of("message", "Movie deleted successfully", "id", id));
    }
}
