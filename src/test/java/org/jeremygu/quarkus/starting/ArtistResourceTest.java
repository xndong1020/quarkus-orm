package org.jeremygu.quarkus.starting;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;

import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.repos.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ArtistResourceTest {

    private ArtistResource resource;
    private ArtistRepository mockRepository;
    private EntityManager mockEntityManager;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        resource = new ArtistResource();
        mockRepository = Mockito.mock(ArtistRepository.class);

        // Use reflection to set the mockRepository into the resource
        Field repositoryField = ArtistResource.class.getDeclaredField("repository");
        repositoryField.setAccessible(true); // Make the private field accessible
        repositoryField.set(resource, mockRepository); // Set the mockRepository to the repository field

        // Mock EntityManager
        mockEntityManager = Mockito.mock(EntityManager.class);

        // Stub getEntityManager() to return the mock EntityManager
        when(mockRepository.getEntityManager()).thenReturn(mockEntityManager);
    }

    @Test
    public void testGetAllArtists() {
        // Mock the PanacheQuery<Artist>
        /**
         * The purpose of this line is to simulate a PanacheQuery<Artist> object
         * so that when methods on the repository (which returns PanacheQuery<Artist>)
         * are called within the test,
         * this mock PanacheQuery object is returned instead of executing any real
         * database operations.
         * This allows you to control the behavior of the query,
         * such as what results are returned or how many results there are,
         * making it possible to test the behavior of your ArtistResource class in
         * isolation from the database.
         */
        PanacheQuery<Artist> mockQuery = Mockito.mock(PanacheQuery.class);
        // When repository.findAll() is called, return the mockQuery
        when(mockRepository.findAll()).thenReturn(mockQuery);
        // Configure mockQuery to return a prepared page when page() is called
        when(mockQuery.page(any(Page.class))).thenReturn(mockQuery);
        // Configure mockQuery to return a list of Artists when list() is called
        when(mockQuery.list()).thenReturn(Arrays.asList(new Artist("Name1", "Bio1"), new Artist("Name2", "Bio2")));
        // Configure mockQuery to return 1 when pageCount() is called
        when(mockQuery.pageCount()).thenReturn(1);
        // Configure mockQuery to return the count of items when count() is called
        when(mockQuery.count()).thenReturn(2L);

        // Execute the method under test
        Response response = resource.getAllArtists(0, 20);

        // Assertions
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Response status should be OK");

        // Assuming you have a mechanism to extract the response entity for inspection
        Map<String, Object> responseEntity = (Map<String, Object>) response.getEntity();
        assertNotNull(responseEntity, "Response entity should not be null");
        assertTrue(responseEntity.containsKey("data"), "Response should contain artist data");
        List<Artist> dataList = (List<Artist>) responseEntity.get("data");
        assertEquals(2, dataList.size(), "Should return 2 artists");
        assertEquals(1, responseEntity.get("totalPages"), "Should report 1 total page");
        assertEquals(2L, responseEntity.get("totalItems"), "Should report 2 total items");
        assertEquals(0, responseEntity.get("currentPage"), "Current page should be 0");
        assertEquals(20, responseEntity.get("currentSize"), "Current size should be 20");
    }

    @Test
    public void testGetArtistByIdFound() {
        Artist artist = new Artist();
        when(mockRepository.findById(1L)).thenReturn(artist);

        Response response = resource.getArtistById(1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(artist, response.getEntity());
    }

    @Test
    public void testGetArtistByIdNotFound() {
        when(mockRepository.findById(1L)).thenReturn(null);

        Response response = resource.getArtistById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCountAllArtists() {
        when(mockRepository.count()).thenReturn(10L);

        long count = resource.countAllArtists();
        assertEquals(10L, count);
    }

    @Test
    public void testUpsertArtistNew() {
        Artist newArtist = new Artist();
        newArtist.name = "New Artist";

        when(mockRepository.isPersistent(newArtist)).thenReturn(false);
        doAnswer(invocation -> {
            ((Artist) invocation.getArgument(0)).artistId = 1L; // Simulate setting ID on persist
            return null;
        }).when(mockRepository).persist(newArtist);

        Response response = resource.upsertArtist(newArtist);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(((Artist) response.getEntity()).artistId);
    }

    @Test
    public void testUpsertArtistUpdate() {
        Artist existingArtist = new Artist();
        existingArtist.artistId = 1L;
        existingArtist.name = "Existing Artist";

        // Stub merge() method of EntityManager
        when(mockEntityManager.merge(existingArtist)).thenReturn(existingArtist);

        when(mockRepository.getEntityManager().merge(existingArtist)).thenReturn(existingArtist);

        Response response = resource.upsertArtist(existingArtist);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(existingArtist, response.getEntity());
    }

    @Test
    public void testDeleteByIdFound() {
        when(mockRepository.deleteById(1L)).thenReturn(true);

        Response response = resource.deleteById(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteByIdNotFound() {
        when(mockRepository.deleteById(1L)).thenReturn(false);

        Response response = resource.deleteById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

}
