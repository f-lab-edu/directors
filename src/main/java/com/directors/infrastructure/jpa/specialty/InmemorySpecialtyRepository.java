package com.directors.infrastructure.jpa.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InmemorySpecialtyRepository implements SpecialtyRepository {

    private final Map<Long, Specialty> specialtyMap = new HashMap<>();
    private long nextId = 1; // 다음 id 값

    @Override
    public Optional<Specialty> findById(Long specialtyId) {
        return Optional.ofNullable(specialtyMap.get(specialtyId));
    }

    @Override
    public Specialty save(Specialty specialty) {
        if (specialty.getId() == null) {
            specialty.setId(nextId++);
        }
        specialtyMap.put(specialty.getId(), specialty);

        return specialty;
    }

    @Override
    public void delete(Long specialtyId) {
        specialtyMap.remove(specialtyId);
    }
}
