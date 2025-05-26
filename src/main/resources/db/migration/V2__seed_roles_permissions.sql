-- Seed default roles
INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN', 'Administrator with full access'),
('ROLE_USER',  'Regular user, can submit reviews');

-- Seed default permissions
INSERT INTO permissions (name) VALUES
('PERM_MOVIE_CREATE'),     -- allow creating/updating movies
('PERM_REVIEW_CREATE'),    -- allow submitting reviews
('PERM_USER_MANAGE');      -- allow managing users

-- Map ROLE_ADMIN to all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN';

-- Map ROLE_USER to only review-creation permission
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'PERM_REVIEW_CREATE'
WHERE r.name = 'ROLE_USER';
