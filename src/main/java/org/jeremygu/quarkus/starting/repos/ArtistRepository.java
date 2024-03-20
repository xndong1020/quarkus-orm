package org.jeremygu.quarkus.starting.repos;

import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.Artist$;

import com.speedment.jpastreamer.application.JPAStreamer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jeremygu.quarkus.starting.models.Film;
import org.jeremygu.quarkus.starting.models.Film$;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    @Inject
    JPAStreamer jpaStreamer;

    // findAll() is provided by PanacheRepository
    // findById(id) is provided by PanacheRepository and returns an Entity instance

    // PanacheRepository provides persist() and delete() methods, so you don't need
    // to implement them.

    // The count() method is also provided by PanacheRepository

    // Additional custom queries can be added as needed.
    // For example, if you need a custom method, you can add it here:

    public Stream<Artist> findByNamePaged(String name, long page, short size) {
        return jpaStreamer.stream(Projection.select(Artist$.name)).filter(Artist$.name.startsWithIgnoreCase(name))
                .sorted(Artist$.name)
                .skip(page * size)
                .limit(size);
    }

    public Optional<Artist> findByStartsWith(String name) {
        return jpaStreamer.stream(Artist.class).filter(Artist$.name.startsWithIgnoreCase(name)).findFirst();
    }

    public Optional<Artist> findByTitleStartingWith(String name) {
        return find("name like ?1", name + "%").firstResultOptional();
    }

    // Other custom queries as needed...
}
