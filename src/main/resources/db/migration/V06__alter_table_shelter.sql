ALTER TABLE t_shelter ADD COLUMN user_id bigint NOT NULL;
ALTER TABLE t_shelter ADD CONSTRAINT f_user_id_shelter_fk FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE;