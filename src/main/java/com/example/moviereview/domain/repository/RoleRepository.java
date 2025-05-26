package com.example.moviereview.domain.repository;

import com.example.moviereview.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Role entities.
 * Provides CRUD and query-by-name for seeding & permission checks.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    /**
     * Find a role by its unique name.
     * Used when assigning roles to users.
     * @param name the role name
     * @return Optional<Role>
     */
    Optional<Role> findByName(String name);
}
