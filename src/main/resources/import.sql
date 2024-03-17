DROP SCHEMA IF EXISTS "test-app" CASCADE;
CREATE SCHEMA "test-app";

CREATE TABLE "test-app".public.artists (
    artist_id BIGSERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    bio TEXT,
    created_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE "test-app".public.films (
    film_id BIGSERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    rating ENUM('G', 'PG', 'PG-13', 'R', 'NC-17'),
    created_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (film_id)
);

CREATE TABLE "test-app".public.films_artists (
    film_id BIGSERIAL NOT NULL,
    artist_id BIGSERIAL NOT NULL,
    PRIMARY KEY (film_id, artist_id),
    FOREIGN KEY (film_id) REFERENCES "test-app".public.films(film_id),
    FOREIGN KEY (artist_id) REFERENCES "test-app".public.artists(artist_id)
);
