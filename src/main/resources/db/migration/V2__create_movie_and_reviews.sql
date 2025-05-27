-- V2__create_movie_and_reviews.sql

-- Create movie table
CREATE TABLE movies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    director VARCHAR(255),
    genre VARCHAR(100),
    year INTEGER NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- Table for movie images (for S3 URL or key)
CREATE TABLE movie_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    movie_id UUID NOT NULL,
    name VARCHAR(128),
    image_url VARCHAR(512) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    CONSTRAINT fk_movie_image_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);

-- Create review table
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    movie_id UUID NOT NULL,
    user_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 10),
    comment TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    CONSTRAINT fk_review_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Indices for performance
CREATE INDEX idx_reviews_movie_id ON reviews(movie_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_movie_images_movie_id ON movie_images(movie_id);

-- Prevent duplicate reviews per user per movie
ALTER TABLE reviews ADD CONSTRAINT unique_review_per_user_per_movie UNIQUE (movie_id, user_id);
