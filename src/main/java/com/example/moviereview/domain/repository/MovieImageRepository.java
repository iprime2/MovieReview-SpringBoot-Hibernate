package com.example.moviereview.domain.repository;

import com.example.moviereview.domain.entity.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieImageRepository extends JpaRepository<MovieImage, UUID> {
}
