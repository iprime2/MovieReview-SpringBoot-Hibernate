package com.example.moviereview.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Permission entity represents a fine-grained action
 * that a role can be granted, e.g. PERM_REVIEW_CREATE.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(generator = "UUID")
    // @GenericGenerator tells Hibernate to use its UUID strategy.
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID") // columnDefinition="UUID" ensures the column is of type UUID in Postgres.
    private UUID id;

    /**
     * Unique name for the permission.
     * Used in authorization checks.
     */
    @Column(unique = true, nullable = false, length = 100)
    private String name;
}
