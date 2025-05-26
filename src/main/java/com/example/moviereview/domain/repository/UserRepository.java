package com.example.moviereview.domain.repository;

import com.example.moviereview.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for User entities.
 * Extends JpaRepository to get CRUD methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Creating custom method
     * Find a user by their email.
     * Used during authentication to load user details.
     *  @param email the user's email
     *  @return Optional<User>
     */
    Optional<User> findByEmail(String email);

    /**
     * Creating Custom Method To Check if a user with the given email already exists.
     */
    boolean existsByEmail(String email);
}
