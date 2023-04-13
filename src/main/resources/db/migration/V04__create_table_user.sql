CREATE TABLE t_user (
    id bigserial NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT t_user_pk PRIMARY KEY (id)
);