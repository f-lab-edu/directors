package com.directors.domain.specialty;

import java.util.Optional;

public interface SpecialtyRepository {
    Optional<Specialty> findSpecialtyByFieldId(String specialtyId);

    void saveSpecialty(Specialty specialty);

    void deleteSpecialty(String specialtyId);

}
