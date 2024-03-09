package org.jeremygu.quarkus.starting.repos;

import jakarta.enterprise.context.ApplicationScoped;
import org.jeremygu.quarkus.starting.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class BookRepository implements GenericRepository<Book, Integer> {

    private final List<Book> books = new ArrayList<>(List.of(
        new Book(1, "My Java book", "Jeremy Gu", 2004, "IT"),
        new Book(2, "My Java book 02", "Jeremy Gu", 2007, "IT")
    ));
    private final AtomicInteger counter = new AtomicInteger(books.size());

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(counter.incrementAndGet());
        }
        books.add(book);
        return book;
    }

    @Override
    public void delete(Book book) {
        books.removeIf(b -> b.getId().equals(book.getId()));
    }

    @Override
    public long count() {
        return books.size();
    }
}
