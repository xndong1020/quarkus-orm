package org.jeremygu.quarkus.starting;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

import org.jeremygu.quarkus.starting.models.Artist;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.CoreMatchers.is;

import java.time.Instant;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArtistResourceTest {

    @Inject
    EntityManager em;

    private Long artistId;

    @BeforeAll
    @Transactional
    public void setup() {
        // Cleanup any existing artists to ensure test isolation
        // em.createQuery("DELETE FROM Artist").executeUpdate();

        // Pre-populate the database if necessary
        Artist artist = new Artist();
        artist.name = "Pre-existing Artist";
        artist.bio = "A pre-existing artist bio";
        artist.createdDate = Instant.now();
        em.persist(artist);
        artistId = artist.id; // Save artist ID for use in tests
    }

    @Test
    @Order(1)
    public void testUpsertArtistEndpoint() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"name\": \"New Artist\", \"bio\": \"A new bio\"}")
                .when().post("/api/artists")
                .then()
                .statusCode(201)
                .body("name", is("New Artist"),
                        "bio", is("A new bio"));
    }

    @Test
    @Order(2)
    public void testGetAllArtistsEndpointWithPagination() {
        RestAssured.given()
                .queryParam("page", 0)
                .queryParam("size", 20)
                .when().get("/api/artists")
                .then()
                .statusCode(200)
                // Update assertions to match the new response structure
                .body("data.size()", is(2)) // Assuming 2 artists exist in the database
                .body("totalPages", is(1)) // Assuming there's only one page of artists
                .body("totalItems", is(2)) // Total number of artists
                .body("currentPage", is(0))
                .body("currentSize", is(20));
    }

    @Test
    @Order(3)
    public void testGetArtistByIdEndpoint() {
        RestAssured.given()
                .when().get("/api/artists/" + artistId)
                .then()
                .statusCode(200)
                .body("name", is("Pre-existing Artist"));
    }

    @Test
    @Order(4)
    public void testCountAllArtistsEndpoint() {
        RestAssured.given()
                .when().get("/api/artists/count")
                .then()
                .statusCode(200)
                .body(is("2"));
    }

    @Test
    @Order(5)
    @Transactional
    public void testDeleteByIdEndpoint() {
        // Ensure the artist to be deleted exists
        RestAssured.given()
                .when().delete("/api/artists/" + artistId)
                .then()
                .statusCode(204); // Expect No Content on successful deletion
    }
}
