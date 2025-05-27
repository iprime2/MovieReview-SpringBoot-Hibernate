package com.example.moviereview.controller;

import com.example.moviereview.dto.ReviewRequest;
import com.example.moviereview.dto.ReviewResponse;
import com.example.moviereview.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST API for movie reviews.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // Create review
    @PreAuthorize("hasAuthority('REVIEW_CREATE')")
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        log.info("[POST /api/reviews] Creating review");
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    // Get review by id
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable UUID id) {
        log.info("[GET /api/reviews/{}] Get review by id", id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    // Get all reviews
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        log.info("[GET /api/reviews] Get all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // Get reviews by movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByMovie(@PathVariable UUID movieId) {
        log.info("[GET /api/reviews/movie/{}] Get reviews by movie", movieId);
        return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId));
    }

    // Get reviews by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable UUID userId) {
        log.info("[GET /api/reviews/user/{}] Get reviews by user", userId);
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    // Update review
    @PreAuthorize("hasAuthority('REVIEW_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable UUID id,
                                                       @Valid @RequestBody ReviewRequest request) {
        log.info("[PUT /api/reviews/{}] Update review", id);
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    // Delete review
    @PreAuthorize("hasAuthority('REVIEW_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReview(@PathVariable UUID id) {
        log.info("[DELETE /api/reviews/{}] Delete review", id);
        reviewService.deleteReview(id);
        return ResponseEntity.ok(Map.of("message", "Review deleted successfully", "id", id));
    }
}
