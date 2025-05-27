package com.example.moviereview.service;

import com.example.moviereview.domain.entity.Movie;
import com.example.moviereview.domain.entity.Review;
import com.example.moviereview.domain.entity.User;
import com.example.moviereview.domain.repository.MovieImageRepository;
import com.example.moviereview.domain.repository.MovieRepository;
import com.example.moviereview.domain.repository.ReviewRepository;
import com.example.moviereview.dto.MovieImageResponse;
import com.example.moviereview.dto.MovieRequest;
import com.example.moviereview.dto.MovieResponse;
import com.example.moviereview.dto.ReviewSummaryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
     private final MovieImageRepository movieImageRepository;

    // Create a new movie
    public MovieResponse createMovie(MovieRequest request) {
        log.info("[POST /api/movies] Creating movie with title: {}", request.getTitle());

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .director(request.getDirector())
                .genre(request.getGenre())
                .year(request.getYear())
                .build();

        Movie saved = movieRepository.save(movie);

        // // Image handling
        // if (request.getImages() != null) {
        //     for (MovieImageRequest imgReq : request.getImages()) {
        //         MovieImage image = MovieImage.builder()
        //             .movie(saved)
        //             .name(imgReq.getName())
        //             .imageUrl(imgReq.getImageUrl())
        //             .build();
        //         movieImageRepository.save(image);
        //     }
        // }

        return mapToResponse(saved);
    }

    // Get movie by ID
    public MovieResponse getMovieById(UUID id) {
        log.info("[GET /api/movies/{}] Fetching movie by id", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + id));
        return mapToResponse(movie);
    }

    // Get all movies
    public List<MovieResponse> getAllMovies() {
        log.info("[GET /api/movies] Fetching all movies");
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update movie
    public MovieResponse updateMovie(UUID id, MovieRequest request) {
        log.info("[PUT /api/movies/{}] Updating movie", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found: " + id));
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDirector(request.getDirector());
        movie.setGenre(request.getGenre());
        movie.setYear(request.getYear());
        Movie updated = movieRepository.save(movie);
        return mapToResponse(updated);
    }

    // Delete movie
    public void deleteMovie(UUID id) {
        log.info("[DELETE /api/movies/{}] Deleting movie", id);
        if (!movieRepository.existsById(id)) {
            log.warn("[DELETE /api/movies/{}] Movie not found", id);
            throw new NoSuchElementException("Movie not found: " + id);
        }
        movieRepository.deleteById(id);
        log.info("[DELETE /api/movies/{}] Deleted movie", id);
    }

    // Map movie entity to response, including review summaries
    private MovieResponse mapToResponse(Movie movie) {
        List<ReviewSummaryResponse> reviewSummaries =
                Optional.ofNullable(movie.getReviews())
                        .orElse(List.of())
                        .stream()
                        .map(this::toReviewSummary)
                        .collect(Collectors.toList());

        List<MovieImageResponse> images =
                Optional.ofNullable(movie.getImages())
                        .orElse(List.of())
                        .stream()
                        .map(img -> new MovieImageResponse(img.getId(), img.getName(), img.getImageUrl()))
                        .collect(Collectors.toList());

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .director(movie.getDirector())
                .genre(movie.getGenre())
                .year(movie.getYear())
                .images(images)
                .reviews(reviewSummaries)
                .createdAt(movie.getCreatedAt())
                .build();
    }

    // Helper to map Review to ReviewSummaryResponse
    private ReviewSummaryResponse toReviewSummary(Review review) {
        User user = review.getUser();
        return ReviewSummaryResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(user.getId())
                .userFullName(user.getFullName())
                .build();
    }
}
