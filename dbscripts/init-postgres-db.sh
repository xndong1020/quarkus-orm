#!/bin/bash
set -e

echo "Creating PostgreSQL database and initializing tables..."

# Connect to the default 'postgres' database to check and drop the existing database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    SELECT 'Checking if database exists...';

    DO \$\$
    BEGIN
        IF EXISTS (SELECT FROM pg_database WHERE datname = 'ua24abwdb_local') THEN
            EXECUTE 'DROP DATABASE "ua24abwdb_local"';
            RAISE NOTICE 'Database dropped successfully.';
        END IF;
    END
    \$\$;

    CREATE DATABASE ua24abwdb_local;
    GRANT ALL PRIVILEGES ON DATABASE ua24abwdb_local TO root;
EOSQL

# Connect to the newly created database to set up the schema and tables
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "ua24abwdb_local" <<-EOSQL
    CREATE SCHEMA uaprod;
    SET search_path TO uaprod;

    CREATE TYPE rating AS ENUM ('G', 'PG', 'PG-13', 'R', 'NC-17');

    CREATE TABLE artists (
        artist_id BIGSERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        bio TEXT,
        created_date TIMESTAMPTZ NOT NULL DEFAULT now()
    );

    CREATE TABLE films (
        film_id BIGSERIAL PRIMARY KEY,
        title VARCHAR(100) NOT NULL,
        description TEXT,
        rating rating,
        created_date TIMESTAMPTZ NOT NULL DEFAULT now()
    );

    CREATE TABLE films_artists (
        film_id BIGINT NOT NULL,
        artist_id BIGINT NOT NULL,
        PRIMARY KEY (film_id, artist_id),
        FOREIGN KEY (film_id) REFERENCES films(film_id),
        FOREIGN KEY (artist_id) REFERENCES artists(artist_id)
    );

    INSERT INTO artists (name, bio)
    SELECT
        'Artist ' || generate_series(1, 10),
        'Bio of artist ' || generate_series(1, 10);

    INSERT INTO films (title, description, rating)
    SELECT
        'Film ' || generate_series(1, 10),
        'Description of film ' || generate_series(1, 10),
        'G' FROM generate_series(1, 10);

    INSERT INTO films_artists (film_id, artist_id)
    SELECT
        f.film_id,
        a.artist_id
    FROM
        (SELECT film_id FROM films ORDER BY random() LIMIT 10) f,
        (SELECT artist_id FROM artists ORDER BY random() LIMIT 10) a;
EOSQL

echo "Database and tables have been successfully created and populated."
