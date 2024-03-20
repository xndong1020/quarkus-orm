package org.jeremygu.quarkus.starting.repos;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jeremygu.quarkus.starting.models.Film;
import org.jeremygu.quarkus.starting.models.Film$;

import java.util.stream.Stream;

@ApplicationScoped
public class FilmRepository implements PanacheRepository<Film> {

    @Inject
    JPAStreamer jpaStreamer;

    public Stream<Film> findByFilmNamePaged(String filmName) {
        final StreamConfiguration<Film> sc = StreamConfiguration.of(Film.class).joining(Film$.artists);
        // return jpaStreamer.stream(sc)
        // .filter(Film$.title.startsWithIgnoreCase(filmName).and(Film$.description.isNotEmpty()))
        // .filter(film -> film.artists.stream().anyMatch(artist ->
        // !artist.name.isEmpty() &&
        // (long) artist.name.charAt(artist.name.length() - 1) > 5))
        // .sorted(Film$.title.reversed());

        return jpaStreamer.stream(sc)
                .filter(Film$.title.startsWithIgnoreCase(filmName).and(Film$.description.isNotEmpty()))
                .filter(film -> film.artists.stream()
                        .allMatch(artist -> (long) artist.name.charAt(artist.name.length() - 1) > 1))
                .sorted(Film$.title.reversed());
    }
}
