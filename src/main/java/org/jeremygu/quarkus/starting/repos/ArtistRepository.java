package org.jeremygu.quarkus.starting.repos;

import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.jeremygu.quarkus.starting.interfaces.IValidationArtistGroups;
import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.Artist$;
import org.jeremygu.quarkus.starting.models.ArtistCreateDTO;
import org.jeremygu.quarkus.starting.models.ArtistDTO;

import com.speedment.jpastreamer.application.JPAStreamer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jeremygu.quarkus.starting.models.Film;
import org.jeremygu.quarkus.starting.models.Film$;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    Validator validator;

    // findAll() is provided by PanacheRepository
    // findById(id) is provided by PanacheRepository and returns an Entity instance

    // PanacheRepository provides persist() and delete() methods, so you don't need
    // to implement them.

    // The count() method is also provided by PanacheRepository

    // Additional custom queries can be added as needed.
    // For example, if you need a custom method, you can add it here:

    public Artist createArtist(ArtistCreateDTO artistDto) throws Exception {
        Set<ConstraintViolation<ArtistCreateDTO>> violations = validator.validate(artistDto,
                IValidationArtistGroups.LargeGroup.class);
        if (violations.isEmpty()) {
            // If Artist extends PanacheEntity or PanacheEntityBase, you can use its
            // instance method to persist
            Artist artist = new Artist(artistDto.name, artistDto.bio);
            artist.persist();
            return artist;
        } else {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new Exception(errorMessages);
        }
    }

    public Artist createArtist(Artist artist) throws Exception {
        artist.persist();
        return artist;
    }

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
