-- insert user
INSERT INTO t_user (email, password, enabled) VALUES
('animal.shelter@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', true),
('lorem@email.com', '{bcrypt}$2a$10$M3YOIcS1JhPLJYT0hHD42u71Zv52bx3ySVJVOavrkbzqarGimp/TC', true);

-- insert user roles
INSERT INTO t_user_roles (user_id, role_id) VALUES (1, 2), (2, 1);

-- insert shelter
INSERT INTO t_shelter (name, deleted, created_at, user_id) VALUES
('Animal Shelter', false, '2023-04-24T10:00:00', 1);

-- insert guardian
INSERT INTO t_guardian (name, deleted, created_at, user_id) VALUES
('Lorem Ipsum', false, '2023-04-24T10:00:00', 2);

-- insert pet
INSERT INTO t_pet (name, description, age, adopted, created_at, shelter_id) VALUES
('Dark', 'A very calm and cute cat', '2 years old', true, '2023-04-26T12:00:00', 1),
('Lala', 'Cute cat 1', '4 month old', false, '2023-04-26T12:00:00', 1);

-- insert image
INSERT INTO t_pet_images (url, pet_id) VALUES
('http://www.imagesxpto.com/cats/1231476517632', 1),
('http://www.imagesxpto.com/cats/2', 2);

-- insert adoption
INSERT INTO t_adoption (date, status, pet_id, guardian_id) VALUES
('2023-04-27T12:00:00', 'CONCLUDED', 1, 1),
('2023-04-27T12:00:00', 'ANALYSING', 1, 1),
('2023-04-27T12:10:00', 'ANALYSING', 1, 1),
('2023-04-27T12:20:00', 'ANALYSING', 1, 1);