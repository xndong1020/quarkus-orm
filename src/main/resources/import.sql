DROP SCHEMA IF EXISTS "test-app" CASCADE;
CREATE SCHEMA "test-app";

CREATE TABLE "test-app".public.t_artists (
    id BIGSERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    bio TEXT,
    created_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);
