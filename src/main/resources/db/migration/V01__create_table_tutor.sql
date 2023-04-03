CREATE TABLE t_tutor (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    enabled bool NOT NULL,
    deleted_at timestamp,
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT t_tutor_pk PRIMARY KEY (id)
);