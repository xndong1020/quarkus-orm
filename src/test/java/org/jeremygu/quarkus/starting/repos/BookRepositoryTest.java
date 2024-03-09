package org.jeremygu.quarkus.starting.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        bookRepository = new BookRepository();
    }

    @Test
    public void testGetAllBooks() {
        var books = bookRepository.findAll();
        assertEquals(2, books.size(), "Expected two books in the list");

        var book1 = books.get(0);
        assertEquals(1, book1.getId());
        assertEquals("My Java book", book1.getTitle());
        assertEquals("Jeremy Gu", book1.getAuthor());
        assertEquals(2004, book1.getYearOfPublication());
        assertEquals("IT", book1.getGenre());

        var book2 = books.get(1);
        assertEquals(2, book2.getId());
        assertEquals("My Java book 02", book2.getTitle());
        assertEquals("Jeremy Gu", book2.getAuthor());
        assertEquals(2007, book2.getYearOfPublication());
        assertEquals("IT", book2.getGenre());
    }

    @Test
    public void testCountAllBooks() {
      var count = bookRepository.count();
        assertEquals(2, count, "Expected book count to be 2");
    }

    @Test
    public void testGetById() {
        var book = bookRepository.findById(1);
        assertTrue(book.isPresent(), "Book with id 1 should be present");
        book.ifPresent(b -> {
            assertEquals(1, b.getId());
            assertEquals("My Java book", b.getTitle());
            assertEquals("Jeremy Gu", b.getAuthor());
            assertEquals(2004, b.getYearOfPublication());
            assertEquals("IT", b.getGenre());
        });

        var missingBook = bookRepository.findById(99);
        assertTrue(missingBook.isEmpty(), "Book with id 99 should not be present");
    }
}
