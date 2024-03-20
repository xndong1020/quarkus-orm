package org.jeremygu.quarkus.starting.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;

import org.jeremygu.quarkus.starting.interfaces.IValidationArtistGroups;
import org.jeremygu.quarkus.starting.models.Artist;
import org.jeremygu.quarkus.starting.models.ArtistCreateDTO;

@ApplicationScoped
public class ArtistService {

    public Artist validateAndMap(
            @Valid @ConvertGroup(to = IValidationArtistGroups.LargeGroup.class) ArtistCreateDTO createDTO) {
        return new Artist(createDTO.name, createDTO.bio, createDTO.createdDate);
    }
}
