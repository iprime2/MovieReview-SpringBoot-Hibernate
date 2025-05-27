package com.example.moviereview.config;

import com.example.moviereview.domain.entity.*;
import com.example.moviereview.domain.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            // 1. All permissions
            List<String> perms = List.of(
                    "USER_CREATE", "USER_DELETE", "USER_UPDATE", "USER_VIEW",
                    "ROLE_CREATE", "ROLE_DELETE", "ROLE_UPDATE", "ROLE_VIEW",
                    "PERMISSION_CREATE", "PERMISSION_DELETE", "PERMISSION_UPDATE", "PERMISSION_VIEW",
                    "MOVIE_CREATE", "MOVIE_DELETE", "MOVIE_UPDATE", "MOVIE_VIEW",
                    "REVIEW_CREATE", "REVIEW_DELETE", "REVIEW_UPDATE", "REVIEW_VIEW"
            );
            Map<String, Permission> permMap = new HashMap<>();
            for (String p : perms) {
                Permission entity = permissionRepository.findByName(p)
                        .orElseGet(() -> permissionRepository.save(Permission.builder().name(p).build()));
                permMap.put(p, entity);
            }

            // 2. Create or update ROLE_ADMIN with all permissions
            Set<Permission> allPerms = new HashSet<>(permMap.values());
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            if (adminRole == null) {
                adminRole = Role.builder()
                        .name("ROLE_ADMIN")
                        .permissions(allPerms)
                        .build();
                adminRole = roleRepository.save(adminRole);
            } else {
                // Update only if not equal
                if (!adminRole.getPermissions().equals(allPerms)) {
                    adminRole.setPermissions(allPerms);
                    roleRepository.save(adminRole);
                }
            }

            // 3. Create or update ROLE_USER with only _VIEW permissions
            Set<Permission> userPerms = perms.stream()
                    .filter(x -> x.endsWith("_VIEW"))
                    .map(permMap::get)
                    .collect(Collectors.toSet());
            Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
            if (userRole == null) {
                userRole = Role.builder()
                        .name("ROLE_USER")
                        .permissions(userPerms)
                        .build();
                userRole = roleRepository.save(userRole);
            } else {
                if (!userRole.getPermissions().equals(userPerms)) {
                    userRole.setPermissions(userPerms);
                    roleRepository.save(userRole);
                }
            }

            // 4. Create admin user if not present
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = User.builder()
                        .email("admin@example.com")
                        .fullName("Admin User")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(Set.of(adminRole))
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }

    @Bean
    public CommandLineRunner seedMovies() {
        return args -> {
            if (movieRepository.count() == 0) {
                ObjectMapper mapper = new ObjectMapper();
                InputStream is = getClass().getClassLoader().getResourceAsStream("movie_seed.json");
                JsonNode node = mapper.readTree(is);

                for (JsonNode movieNode : node) {
                    Movie movie = Movie.builder()
                            .title(movieNode.get("title").asText())
                            .year(movieNode.get("year").asInt())
                            .genre(movieNode.get("genre").asText())
                            .description(movieNode.has("description") ? movieNode.get("description").asText(null) : null)
                            .director(movieNode.has("director") ? movieNode.get("director").asText(null) : null)
                            .build();

                    List<MovieImage> images = new ArrayList<>();
                    for (JsonNode imgNode : movieNode.get("images")) {
                        MovieImage image = MovieImage.builder()
                                .name(imgNode.get("name").asText())
                                .imageUrl(imgNode.get("imageUrl").asText())
                                .movie(movie)
                                .build();
                        images.add(image);
                    }
                    movie.setImages(images);

                    // Save movie and images (cascade will save images if set properly)
                    movieRepository.save(movie);
//                    log.info("Seeded movie: {}", movie.getTitle());
                }
            }
        };
    }
}
