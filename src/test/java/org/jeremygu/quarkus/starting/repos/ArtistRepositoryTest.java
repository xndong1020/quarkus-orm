package org.jeremygu.quarkus.starting.repos;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jeremygu.quarkus.starting.models.Artist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@QuarkusTest
public class ArtistRepositoryTest {

    @Inject
    ArtistRepository artistRepository;

    @Test
    @Transactional
    public void testFindAll() throws SQLException {
        // Given
        Artist artist = new Artist("Artist Name", "Artist Bio", Instant.now());
        artist.name = "Artist Name";
        artist.bio = "Artist Bio";
        artist.createdDate = Instant.now();
        artistRepository.persist(artist);

        // When
        List<Artist> artists = artistRepository.listAll();

        // Then
        Assertions.assertFalse(artists.isEmpty(), "Artists list should not be empty after adding an artist.");
    }

    @Test
    @Transactional
    public void testSaveAndFindById() {
        // Given
        Artist newArtist = new Artist();
        newArtist.name = "Test Artist";
        newArtist.bio = "Bio";
        newArtist.createdDate = Instant.now();

        // When
        artistRepository.persist(newArtist);
        Artist foundArtist = artistRepository.findById(newArtist.id);

        // Then
        Assertions.assertNotNull(foundArtist, "Saved artist should be found with findById");
        Assertions.assertEquals("Test Artist", foundArtist.name, "Artist name should match");
        Assertions.assertEquals("Bio", foundArtist.bio, "Artist bio should match");
    }

    @Test
    @Transactional
    public void testCount() {
        // Given - initial empty state enforced by setup()

        // When
        long countBefore = artistRepository.count();
        Artist artist = new Artist();
        artist.name = "Another Artist";
        artist.bio = "Another Bio";
        artist.createdDate = Instant.now();
        artistRepository.persist(artist);

        // Then
        long countAfter = artistRepository.count();
        Assertions.assertEquals(countBefore + 1, countAfter, "Count should increase by 1 after adding an artist");
    }

    @Test
    @Transactional
    public void testDeleteByIdEndpoint() {
        // Given
        Artist artist = new Artist();
        artist.name = "Artist to Delete";
        artist.bio = "Bio";
        artist.createdDate = Instant.now();
        artistRepository.persist(artist);
        long initialCount = artistRepository.count();

        // When
        artistRepository.delete(artist);

        // Then
        long finalCount = artistRepository.count();
        Assertions.assertEquals(initialCount - 1, finalCount, "Count should decrease by 1 after deletion");
    }
}
