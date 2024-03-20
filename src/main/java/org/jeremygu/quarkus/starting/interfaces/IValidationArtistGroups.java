package org.jeremygu.quarkus.starting.interfaces;


import jakarta.validation.groups.Default;

public interface IValidationArtistGroups {
    interface SmallGroup extends Default {}

    interface LargeGroup extends Default {}
}
