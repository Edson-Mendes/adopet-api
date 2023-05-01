-- insert user
INSERT INTO t_user (email, password, enabled) VALUES
('animal.shelter@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', true),
('shelterpoa@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', false);

-- insert user roles
INSERT INTO t_user_roles (user_id, role_id) VALUES (1, 2), (2, 2);

-- insert shelter
INSERT INTO t_shelter (name, deleted, created_at, user_id) VALUES
('Animal Shelter', false, '2023-04-24T10:00:00', 1),
('Shelter POA', true, '2023-04-25T10:00:00', 2);

-- insert pet
INSERT INTO t_pet (name, description, age, adopted, created_at, shelter_id) VALUES
('Dark', 'A very calm and cute cat', '2 years old', false, '2023-04-26T12:00:00', 1),
('Darkness', 'A cute cat', '2 years old', false, '2023-04-26T13:00:00', 1),
('Fluffy', 'A fluffy cat', '4 years old', true, '2023-04-27T13:00:00', 1),
('King', 'A king cat', '8 years old', true, '2023-04-27T13:00:00', 2),
('Prince', 'A prince cat', '3 years old', false, '2023-04-27T13:00:00', 2);

-- insert image
INSERT INTO t_pet_images (url, pet_id) VALUES
('http://www.imagesxpto.com/cats/1', 1),
('http://www.imagesxpto.com/cats/2', 2),
('http://www.imagesxpto.com/cats/3', 3),
('http://www.imagesxpto.com/cats/4', 4),
('http://www.imagesxpto.com/cats/5', 5);