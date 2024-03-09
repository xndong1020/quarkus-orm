package org.jeremygu.quarkus.starting;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.CoreMatchers.is;

import java.sql.SQLException;

import org.jeremygu.quarkus.starting.repos.DatabaseUtil;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Define the order of test methods

public class ArtistResourceTest {

  @Inject
    DatabaseUtil dbUtil;

    @BeforeAll
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
  @Order(1)
  public void testUpsertArtistEndpoint() {
      RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\"name\": \"New Artist\", \"bio\": \"A new bio\"}")
        .when().post("/api/artists")
        .then()
           .statusCode(200)
           .body("name", is("New Artist"),
                 "bio", is("A new bio")); // Adjust based on your implementation
  }

    @Test
    @Order(2)
    public void testGetAllArtistsEndpoint() {
        RestAssured.given()
          .when().get("/api/artists")
          .then()
             .statusCode(200)
             .body("$.size()", is(1)); // Assuming there are 2 artists in your database
    }

    @Test
    @Order(3)
    public void testGetArtistByIdEndpoint() {
        RestAssured.given()
          .when().get("/api/artists/1")
          .then()
             .statusCode(200)
             .body("id", is(1)); // Adjust expectations based on your data
    }

    @Test
    @Order(4)
    public void testCountAllArtistsEndpoint() {
        RestAssured.given()
          .when().get("/api/artists/count")
          .then()
             .statusCode(200)
             .body(is("1")); // Assuming there are 2 artists
    }


    @Test
    @Order(5)
    public void testDeleteByIdEndpoint() {
        // This test might need to be adjusted based on how your application handles deletes
        // For example, if deletion is permanent, you might first need to ensure an artist exists to delete
        RestAssured.given()
          .when().delete("/api/artists/1")
          .then()
             .statusCode(204); // No Content on successful deletion
    }
}
