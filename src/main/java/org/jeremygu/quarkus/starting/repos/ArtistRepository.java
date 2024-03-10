package org.jeremygu.quarkus.starting.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

import org.jeremygu.quarkus.starting.models.Artist;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    // findAll() is provided by PanacheRepository
    // findById(id) is provided by PanacheRepository and returns an Entity instance

    // PanacheRepository provides persist() and delete() methods, so you don't need
    // to implement them.

    // The count() method is also provided by PanacheRepository

    // Additional custom queries can be added as needed.
    // For example, if you need a custom method, you can add it here:

    public List<Artist> findByName(String name) {
        return find("name", name).list();
    }

    // Other custom queries as needed...
}
