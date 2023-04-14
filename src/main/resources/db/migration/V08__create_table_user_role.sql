CREATE TABLE t_user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES t_user(id),
	CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES t_role(id)
)