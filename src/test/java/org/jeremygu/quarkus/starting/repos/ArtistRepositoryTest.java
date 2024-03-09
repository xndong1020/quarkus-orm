package org.jeremygu.quarkus.starting.repos;

import io.quarkus.test.junit.QuarkusTest;

import org.jeremygu.quarkus.starting.models.Artist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;


@QuarkusTest
public class ArtistRepositoryTest {

    @Inject
    ArtistRepository artistRepository;

    @Inject
    DatabaseUtil dbUtil;

    @BeforeEach
    public void setup() throws SQLException {
        // Drop the table if it exists to ensure a clean state
        dbUtil.executeSql("DROP TABLE IF EXISTS public.t_artists");

        // Recreate the table
        dbUtil.executeSql("""
            CREATE table public.t_artists (
                id BIGSERIAL NOT NULL,
                "name" VARCHAR(100) NOT NULL,
                bio TEXT NULL,
                created_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                PRIMARY KEY (id)
            )
            """);
    }

    @Test
    public void testFindAll() throws SQLException {
        // Given pre-populated data or after inserting data

        // When
        List<Artist> artists = artistRepository.findAll();

        // Then
        Assertions.assertNotNull(artists);
        Assertions.assertTrue(artists.isEmpty());
    }

    @Test
    public void testSaveAndFindById() {
        // Given
        Artist newArtist = new Artist(null, "Test Artist", "Bio", Instant.now());

        // When
        Artist savedArtist = artistRepository.save(newArtist);
        Artist foundArtist = artistRepository.findById(savedArtist.getId()).orElse(null);

        // Then
        Assertions.assertNotNull(foundArtist);
        Assertions.assertEquals("Test Artist", foundArtist.getName());
        Assertions.assertEquals("Bio", foundArtist.getBio());
    }

    @Test
    public void testCount() {
        // Given pre-populated data or after inserting data

        // When
        long count = artistRepository.count();

        // Then
        Assertions.assertTrue(count >= 0);
    }
}
