package com.directors.domain.specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository {
    Optional<Specialty> findById(Long specialtyId);

    List<Specialty> findByUserId(String userId);

    void save(Specialty specialty);

    void delete(Long specialtyId);
}
