package com.example.moviereview.domain.repository;

import com.example.moviereview.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByMovieId(UUID movieId);
    List<Review> findByUserId(UUID userId);
}
