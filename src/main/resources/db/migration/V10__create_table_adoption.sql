CREATE TABLE t_adoption (
    id bigserial NOT NULL,
    pet_id bigint NOT NULL,
    guardian_id bigint NOT NULL,
    date timestamp NOT NULL,
    CONSTRAINT t_adoption_pk PRIMARY KEY (id),
    CONSTRAINT f_pet_id_fk FOREIGN KEY (pet_id) REFERENCES t_pet(id),
    CONSTRAINT f_guardian_id_id_fk FOREIGN KEY (guardian_id) REFERENCES t_guardian(id)
);