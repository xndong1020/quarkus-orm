package org.jeremygu.quarkus.starting.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jeremygu.quarkus.starting.interfaces.IValidationArtistGroups;

import java.time.Instant;

public class ArtistCreateDTO {

    public Long artistId;

    @NotBlank(message = "The name of artist must not be empty")
    @Size(min = 2, groups = IValidationArtistGroups.SmallGroup.class, message = "Name should have at least 2 chars")
    @Size(min = 5, groups = IValidationArtistGroups.LargeGroup.class, message = "Name should have at least 5 chars")
    public String name;

    @Size(max = 10, message = "Bio should have no more than 10 chars")
    public String bio;

    public Instant createdDate = Instant.now();
}
