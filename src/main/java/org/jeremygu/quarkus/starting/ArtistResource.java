package org.jeremygu.quarkus.starting;

import java.util.List;

import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.repos.ArtistRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
  public List<Artist> getAllArtists() {
    return repository.listAll(); // Use Panache's listAll method
  }

  @GET
  @Path("{id}")
  public Response getArtistById(@PathParam("id") Long id) {
    Artist artist = repository.findById(id); // Panache's findById directly returns the entity
    if (artist != null) {
      return Response.ok(artist).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public long countAllArtists() {
    return repository.count(); // Use Panache's count method
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public Response upsertArtist(Artist artist) {
    if (artist.id == null) {
      repository.persist(artist); // Persist new artist
      return Response.status(Response.Status.CREATED).entity(artist).build();
    } else {
      Artist updatedArtist = repository.getEntityManager().merge(artist); // Update existing artist
      return Response.ok(updatedArtist).build();
    }
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response deleteById(@PathParam("id") Long id) {
    boolean deleted = repository.deleteById(id); // Panache provides deleteById
    if (deleted) {
      return Response.status(Response.Status.NO_CONTENT).build(); // Successfully deleted
    } else {
      return Response.status(Response.Status.NOT_FOUND).build(); // Artist not found
    }
  }
}
