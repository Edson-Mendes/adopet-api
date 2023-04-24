CREATE TABLE t_pet_images (
    id bigserial NOT NULL,
    url varchar(255) NOT NULL,
    pet_id bigint NOT NULL,
    CONSTRAINT t_pet_images_pk PRIMARY KEY (id),
    CONSTRAINT t_pet_images_f_pet_id_fk FOREIGN KEY (pet_id) REFERENCES t_pet(id)
);