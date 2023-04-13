CREATE TABLE t_shelter (
    id bigserial NOT NULL,
    name varchar(100) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT t_shelter_pk PRIMARY KEY (id)
);