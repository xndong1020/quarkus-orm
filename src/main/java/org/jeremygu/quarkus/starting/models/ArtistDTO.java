package org.jeremygu.quarkus.starting.models;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistDTO {
    public Long artistId;
    public String name;
    public String bio;
    public List<FilmDTO> films;  // Use FilmDTO to avoid cyclic references

    public ArtistDTO(Artist artist) {
        this.artistId = artist.artistId;
        this.name = artist.name;
        this.bio = artist.bio;
        this.films = artist.films.stream()
                .map(FilmDTO::new)
                .collect(Collectors.toList());
    }
}
