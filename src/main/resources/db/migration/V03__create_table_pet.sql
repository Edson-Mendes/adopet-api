CREATE TABLE t_pet (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    age smallint NOT NULL,
    description varchar(255) NOT NULL,
    adopted bool NOT NULL,
    created_at timestamp NOT NULL,
    animal_shelter_id int8 NOT NULL,
    CONSTRAINT t_pet_pk PRIMARY KEY (id),
    CONSTRAINT shelter_id_fk FOREIGN KEY (animal_shelter_id) REFERENCES t_animal_shelter(id)
);