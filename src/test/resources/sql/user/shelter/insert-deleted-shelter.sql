-- insert user
INSERT INTO t_user (email, password, enabled) VALUES
('animal.shelter@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', false);

-- insert user roles
INSERT INTO t_user_roles (user_id, role_id) VALUES (1, 2);

-- insert shelter
INSERT INTO t_shelter (name, deleted, created_at, user_id) VALUES
('Animal Shelter', true, '2023-04-24T10:00:00', 1);
