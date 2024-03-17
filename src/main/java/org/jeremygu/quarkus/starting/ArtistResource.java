package org.jeremygu.quarkus.starting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.ArtistDTO;
import org.jeremygu.quarkus.starting.repos.ArtistRepository;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

@Path("/api/artists")
@Produces(MediaType.APPLICATION_JSON)
public class ArtistResource {

  @Inject
  private ArtistRepository repository;

  @GET
  public Response getAllArtists(@QueryParam("page") @DefaultValue("0") int page,
      @QueryParam("size") @DefaultValue("20") int size) {
    PanacheQuery<Artist> query = repository.findAll().page(Page.of(page, size));
    List<Artist> artists = query.list();

    Map<String, Object> response = new HashMap<>();
    response.put("data", artists);
    response.put("totalPages", query.pageCount());
    response.put("totalItems", query.count());
    response.put("currentPage", page);
    response.put("currentSize", size);

    return Response.ok(response).build();
  }

  @GET
  @Path("{id}")
  public Response getArtistById(@PathParam("id") Long id) {
    Artist artist = repository.findById(id); // Panache's findById directly returns the entity
    if (artist != null) {
      var artistDTO = new ArtistDTO(artist);
      return Response.ok(artistDTO).build();
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
    if (artist.artistId == null) {
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
