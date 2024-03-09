package org.jeremygu.quarkus.starting.repos;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jeremygu.quarkus.starting.models.Artist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@QuarkusTest
public class ArtistRepositoryTest {

    @Inject
    ArtistRepository artistRepository;

    @BeforeEach
    @Transactional
    public void setup() throws SQLException {
        // Clean up the artist table before each test
        // artistRepository.findAll().forEach(artist -> artistRepository.delete(artist));
    }

    @Test
    public void testFindAll() throws SQLException {
        // Given
        Artist artist = new Artist();
        artist.setName("Artist Name");
        artist.setBio("Artist Bio");
        artist.setCreatedDate(Instant.now());
        artistRepository.save(artist);

        // When
        List<Artist> artists = artistRepository.findAll();

        // Then
        Assertions.assertFalse(artists.isEmpty(), "Artists list should not be empty after adding an artist.");
    }

    @Test
    @Transactional
    public void testSaveAndFindById() {
        // Given
        Artist newArtist = new Artist();
        newArtist.setName("Test Artist");
        newArtist.setBio("Bio");
        newArtist.setCreatedDate(Instant.now());

        // When
        artistRepository.save(newArtist);
        Artist foundArtist = artistRepository.findById(newArtist.getId()).orElse(null);

        // Then
        Assertions.assertNotNull(foundArtist, "Saved artist should be found with findById");
        Assertions.assertEquals("Test Artist", foundArtist.getName(), "Artist name should match");
        Assertions.assertEquals("Bio", foundArtist.getBio(), "Artist bio should match");
    }

    @Test
    public void testCount() {
        // Given - initial empty state enforced by setup()

        // When
        long countBefore = artistRepository.count();
        Artist artist = new Artist();
        artist.setName("Another Artist");
        artist.setBio("Another Bio");
        artist.setCreatedDate(Instant.now());
        artistRepository.save(artist);

        // Then
        long countAfter = artistRepository.count();
        Assertions.assertEquals(countBefore + 1, countAfter, "Count should increase by 1 after adding an artist");
    }

    @Test
    @Transactional
    public void testDeleteByIdEndpoint() {
        // Given
        Artist artist = new Artist();
        artist.setName("Artist to Delete");
        artist.setBio("Bio");
        artist.setCreatedDate(Instant.now());
        artistRepository.save(artist);
        long initialCount = artistRepository.count();

        // When
        artistRepository.delete(artist);

        // Then
        long finalCount = artistRepository.count();
        Assertions.assertEquals(initialCount - 1, finalCount, "Count should decrease by 1 after deletion");
    }
}
