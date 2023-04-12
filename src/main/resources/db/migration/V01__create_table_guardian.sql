CREATE TABLE t_guardian (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT t_guardian_pk PRIMARY KEY (id)
);