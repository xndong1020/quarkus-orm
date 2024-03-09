package org.jeremygu.quarkus.starting;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.jeremygu.quarkus.starting.models.Book;
import org.jeremygu.quarkus.starting.repos.BookRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    private BookRepository repository;
    @Inject
    private Logger logger;

    @GET
    public List<Book> getAllBooks() {
        return this.repository.findAll();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public long countAllBooks() {
        return this.repository.count();
    }

    @GET
    @Path("{id}")
    public Optional<Book> getBookById(@PathParam("id") int id) {
        logger.info("id is %d".formatted(id));
        return this.repository.findById(id);
    }
}
