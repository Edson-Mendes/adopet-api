-- insert user
INSERT INTO t_user (email, password, enabled) VALUES
('lorem@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', true),
('dolor@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', true);

-- insert user roles
INSERT INTO t_user_roles (user_id, role_id) VALUES (1, 1), (2, 1);

-- insert shelter
INSERT INTO t_guardian (name, deleted, created_at, user_id) VALUES
('Lorem Ipsum', false, '2023-04-24T10:00:00', 1),
('Dolor Set', false, '2023-04-25T10:00:00', 2);

