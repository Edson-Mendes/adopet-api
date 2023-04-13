ALTER TABLE t_guardian ADD COLUMN user_id bigint NOT NULL;
ALTER TABLE t_guardian ADD CONSTRAINT f_user_id_fk FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE;