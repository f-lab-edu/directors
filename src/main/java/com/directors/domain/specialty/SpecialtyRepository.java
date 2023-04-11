package com.directors.domain.specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository {
    Optional<Specialty> findByFieldId(String specialtyId);

    List<Specialty> findByUserId(String userId);

    void save(Specialty specialty);

    void delete(String specialtyId);

}
