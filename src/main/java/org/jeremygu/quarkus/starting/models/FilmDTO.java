package org.jeremygu.quarkus.starting.models;

public class FilmDTO {
    public Long filmId;
    public String title;
    public String description;

    public FilmDTO(Film film) {
        this.filmId = film.filmId;
        this.title = film.title;
        this.description = film.description;
    }
}
