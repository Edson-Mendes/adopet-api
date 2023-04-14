CREATE TABLE t_role (
	id serial NOT NULL,
	name varchar(25) NOT NULL,
	CONSTRAINT role_name_unique UNIQUE (name),
	CONSTRAINT role_id_pk PRIMARY KEY (id)
);