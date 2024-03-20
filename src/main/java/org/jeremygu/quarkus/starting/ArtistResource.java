package org.jeremygu.quarkus.starting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.ArtistCreateDTO;
import org.jeremygu.quarkus.starting.models.ArtistDTO;
import org.jeremygu.quarkus.starting.repos.ArtistRepository;
import org.jeremygu.quarkus.starting.repos.FilmRepository;

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
import jakarta.ws.rs.core.Response.Status;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jeremygu.quarkus.starting.services.ArtistService;

@Path("/api/artists")
@Produces(MediaType.APPLICATION_JSON)
public class ArtistResource {

  @Inject
  ArtistRepository artistRepository;

  @Inject
  FilmRepository filmRepository;

  @Inject
  ArtistService artistService;

  @GET
  public Response getAllArtists(@QueryParam("page") @DefaultValue("0") int page,
      @QueryParam("size") @DefaultValue("20") int size) {
    PanacheQuery<Artist> query = artistRepository.findAll().page(Page.of(page, size));
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
    Artist artist = artistRepository.findById(id); // Panache's findById directly returns the entity
    if (artist != null) {
      var artistDTO = new ArtistDTO(artist);
      return Response.ok(artistDTO).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/name/{name}")
  public Response getArtistByName(@PathParam("name") String name) {
    Artist artist = artistRepository.findByStartsWith(name).orElse(null); // Panache's findById directly returns the
                                                                          // entity
    // Artist artist = repository.findByTitleStartingWith(name).orElse(null); // JPA
    // Streamer
    // PanacheRepository

    if (artist != null) {
      return Response.ok(artist).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/pagedName/{name}")
  public Response getArtistByName(@PathParam("name") String name, @QueryParam("page") @DefaultValue("0") long page,
      @QueryParam("size") @DefaultValue("20") short size) {

    var artists = artistRepository.findByNamePaged(name, page, size)
        .collect(Collectors.toCollection(ArrayList::new));

    return Response.ok(artists).build();
  }

  @GET
  @Path("/actors/{startsWith}")
  public Response getFilmsByName(@PathParam("startsWith") String nameStartsWith) {

    var films = filmRepository.findByFilmNamePaged(nameStartsWith)
        .collect(Collectors.toCollection(ArrayList::new));

    return Response.ok(films).build();
  }

  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public long countAllArtists() {
    return artistRepository.count(); // Use Panache's count method
  }

  // @POST
  // @Consumes(MediaType.APPLICATION_JSON)
  // @Produces(MediaType.APPLICATION_JSON)
  // @Transactional
  // public Response createArtist(@Valid ArtistCreateDTO artist) {
  // Artist artistInDb = artistRepository.createArtist(artist); // Persist new
  // artist
  // return Response.status(Response.Status.CREATED).entity(artistInDb).build();
  // }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @Transactional
  public Response createArtist(ArtistCreateDTO artist) throws Exception {
    try {
      Artist artistToSave = artistService.validateAndMap(artist);
      Artist artistInDb = artistRepository.createArtist(artistToSave);
      return Response.status(Response.Status.CREATED).entity(artistInDb).build();
    } catch (ConstraintViolationException e) {
      String violations = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
          .collect(Collectors.joining(", "));
      return Response.status(Status.BAD_REQUEST).entity(violations).build();
    }
  }

  // @POST
  // @Consumes(MediaType.APPLICATION_JSON)
  // @Produces(MediaType.APPLICATION_JSON)
  // @Transactional
  // public Response upsertArtist(ArtistCreateDTO artist) throws Exception {
  // if (artist.artistId == null) {
  // Artist artistInDb = artistRepository.createArtist(artist); // Persist new
  //
  // return Response.status(Response.Status.CREATED).entity(artistInDb).build();
  // } else {
  // Artist updatedArtist = artistRepository.getEntityManager()
  // .merge(new Artist(artist.artistId, artist.name, artist.bio,
  // artist.createdDate)); // Update existing artist
  // return Response.ok(updatedArtist).build();
  // }
  // }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response deleteById(@PathParam("id") Long id) {
    boolean deleted = artistRepository.deleteById(id); // Panache provides deleteById
    if (deleted) {
      return Response.status(Response.Status.NO_CONTENT).build(); // Successfully deleted
    } else {
      return Response.status(Response.Status.NOT_FOUND).build(); // Artist not found
    }
  }
}
