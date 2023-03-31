package com.directors.infrastructure.jpa.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InmemorySpecialtyRepository implements SpecialtyRepository {

    private final Map<String, Specialty> specialtyMap = new HashMap<>();
    private long nextId = 1; // 다음 id 값

    @Override
    public Specialty findSpecialtyByFieldId(String specialtyId) {
        Specialty field = specialtyMap.get(specialtyId);
        return field != null ? field : null;
    }

    @Override
    public void saveSpecialty(Specialty specialty) {
        if (specialty.getId() == null) {
            specialty.setId(String.valueOf(nextId++));
        }
        specialtyMap.put(specialty.getId(), specialty);
    }

    @Override
    public void deleteSpecialty(String specialtyId) {
        specialtyMap.remove(specialtyId);
    }
}