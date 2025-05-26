package com.example.moviereview.domain.repository;

import com.example.moviereview.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Permission entities.
 * Allows lookup by permission name when building role → permission mappings.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    /**
     * Find a permission by its unique name.
     * Useful when seeding default permissions or checking authorization.
     * @param name the permission name
     * @return Optional<Permission>
     */
    Optional<Permission> findByName(String name);
}
