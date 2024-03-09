package org.jeremygu.quarkus.starting;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class BookResourceTest {
    public void testGetAllBooksEndpoint() {
        RestAssured.given()
          .when().get("/api/books")
          .then()
             .statusCode(200)
             .body("$.size()", is(2), // Asserts the size of the list
                   "[0].id", is(1),
                   "[0].title", is("My Java book"),
                   "[0].author", is("Jeremy Gu"),
                   "[0].yearOfPublication", is(2004), // Assuming the correct property name
                   "[0].genre", is("IT"),
                   "[1].id", is(2),
                   "[1].title", is("My Java book 02"),
                   "[1].author", is("Jeremy Gu"),
                   "[1].yearOfPublication", is(2007),
                   "[1].genre", is("IT"));
    }

    @Test
    public void testCountAllBooksEndpoint() {
        RestAssured.given()
          .when().get("/api/books/count")
          .then()
             .statusCode(200)
             .body(is("2"));
    }

    @Test
    public void testGetBookByIdEndpoint() {
        RestAssured.given()
          .when().get("/api/books/1")
          .then()
             .statusCode(200)
             .body("id", is(1))
             .body("title", is("My Java book"))
             .body("author", is("Jeremy Gu"))
             .body("yearOfPublication", is(2004))
             .body("genre", is("IT"));
    }
}