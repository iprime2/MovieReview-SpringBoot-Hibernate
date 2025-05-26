package com.example.moviereview.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Role entity represents a security role in the system,
 * e.g. ROLE_ADMIN or ROLE_USER.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(generator = "UUID")
    // @GenericGenerator tells Hibernate to use its UUID strategy.
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID") // columnDefinition="UUID" ensures the column is of type UUID in Postgres.
    private UUID id;

    /** Unique name of the role, used in security checks. */
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    /** Optional description for humans/admin UI. */
    @Column(length = 200)
    private String description;

    /**
     * Many-to-many link to permissions.
     * LAZY fetch because we rarely need all permissions when simply loading roles.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
