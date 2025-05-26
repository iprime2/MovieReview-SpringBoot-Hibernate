package com.example.moviereview.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User entity represents an application user who can log in,
 * submit reviews, and (depending on roles) manage movies/users.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
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
     * Email address, used as username for authentication.
     * Must be unique and not null.
     */
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    /**
     * Encrypted password which will be in hash.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User’s full name, for display in the UI/profile.
     */
    @Column(name = "full_name", length = 100)
    private String fullName;

    /**
     * Whether the account is enabled.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Timestamp when the user was created which will automatically inserted.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Roles granted to the user.
     * EAGER fetch because roles are needed immediately during authentication.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
