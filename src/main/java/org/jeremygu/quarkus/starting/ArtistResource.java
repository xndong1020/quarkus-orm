package org.jeremygu.quarkus.starting;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.Book;
import org.jeremygu.quarkus.starting.repos.ArtistRepository;
import org.jeremygu.quarkus.starting.repos.BookRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/artists")
@Produces(MediaType.APPLICATION_JSON)

public class ArtistResource {

    @Inject
    private ArtistRepository repository;

    @GET
    public List<Artist> getAllArtists() throws SQLException {
      return this.repository.findAll();
    }

    @GET
    @Path("{id}")
    public Optional<Artist> getArtistById(@PathParam("id") Long id) {
      return this.repository.findById(id);
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public long countAllArtists() {
      return this.repository.count();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Artist upsertArtist(Artist artist) {
        var createdArtist = this.repository.save(artist);
        return createdArtist;
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        var artistOptional = this.repository.findById(id);
        if (artistOptional.isPresent()) {
            this.repository.delete(artistOptional.get());
            return Response.status(Response.Status.NO_CONTENT).build(); // Successfully deleted
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // Artist not found
        }
    }
}
