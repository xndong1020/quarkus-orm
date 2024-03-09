package org.jeremygu.quarkus.starting.repos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.jeremygu.quarkus.starting.models.Artist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ArtistRepository implements GenericRepository<Artist, Long> {

    @Inject
    EntityManager em;

    @Inject
    private Logger logger;

    @Override
    public List<Artist> findAll() throws SQLException {
        return em.createQuery("SELECT a FROM Artist a", Artist.class).getResultList();
    }

    @Override
    public Optional<Artist> findById(Long id) {
        var artist = em.find(Artist.class, id);
        return Optional.ofNullable(artist);
    }

    @Override
    @Transactional
    public Artist save(Artist entity) {
        if (entity.getId() == null) {
            em.persist(entity); // Persist new artist
        } else {
            entity = em.merge(entity); // Update existing artist
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(Artist entity) {
        var managedArtist = em.merge(entity);
        em.remove(managedArtist);
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(a) FROM Artist a", Long.class)
                .getSingleResult();
    }
}
