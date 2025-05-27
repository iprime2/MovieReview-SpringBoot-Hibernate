package com.example.moviereview.service;

import com.example.moviereview.domain.entity.Movie;
import com.example.moviereview.domain.entity.Review;
import com.example.moviereview.domain.entity.User;
import com.example.moviereview.domain.repository.MovieRepository;
import com.example.moviereview.domain.repository.ReviewRepository;
import com.example.moviereview.domain.repository.UserRepository;
import com.example.moviereview.dto.ReviewRequest;
import com.example.moviereview.dto.ReviewResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing reviews.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    /**
     * Create a new review.
     */
    public ReviewResponse createReview(ReviewRequest request) {
        log.info("[POST /api/reviews] Creating review for movie: {}, user: {}", request.getMovieId(), request.getUserId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + request.getUserId()));
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + request.getMovieId()));
        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    /**
     * Get a review by ID.
     */
    public ReviewResponse getReviewById(UUID id) {
        log.info("[GET /api/reviews/{}] Fetching review", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found: " + id));
        return mapToResponse(review);
    }

    /**
     * Get all reviews.
     */
    public List<ReviewResponse> getAllReviews() {
        log.info("[GET /api/reviews] Fetching all reviews");
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all reviews for a movie.
     */
    public List<ReviewResponse> getReviewsByMovie(UUID movieId) {
        log.info("[GET /api/reviews/movie/{}] Fetching reviews by movie", movieId);
        return reviewRepository.findByMovieId(movieId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all reviews by a user.
     */
    public List<ReviewResponse> getReviewsByUser(UUID userId) {
        log.info("[GET /api/reviews/user/{}] Fetching reviews by user", userId);
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update a review.
     */
    public ReviewResponse updateReview(UUID id, ReviewRequest request) {
        log.info("[PUT /api/reviews/{}] Updating review", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found: " + id));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review updated = reviewRepository.save(review);
        return mapToResponse(updated);
    }

    /**
     * Delete a review.
     */
    public void deleteReview(UUID id) {
        log.info("[DELETE /api/reviews/{}] Deleting review", id);
        if (!reviewRepository.existsById(id)) {
            log.warn("[DELETE /api/reviews/{}] Review not found", id);
            throw new NoSuchElementException("Review not found: " + id);
        }
        reviewRepository.deleteById(id);
        log.info("[DELETE /api/reviews/{}] Deleted review", id);
    }

    /**
     * Map entity to DTO.
     */
    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
