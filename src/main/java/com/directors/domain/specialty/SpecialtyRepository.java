package com.directors.domain.specialty;

import java.util.Optional;

public interface SpecialtyRepository {
    Optional<Specialty> findById(Long specialtyId);

    void save(Specialty specialty);

    void delete(Long specialtyId);
}
