package com.directors.infrastructure.jpa.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InmemorySpecialtyRepository implements SpecialtyRepository {

    private final Map<Long, Specialty> specialtyMap = new HashMap<>();
    private long nextId = 1; // 다음 id 값

    @Override
    public Optional<Specialty> findById(Long specialtyId) {
        return Optional.ofNullable(specialtyMap.get(specialtyId));
    }

    @Override
    public List<Specialty> findByUserId(String userId) {
        return null;
//        return specialtyMap.values()
//                .stream()
//                .filter(sp -> sp.getUserId().equals(userId))
//                .collect(Collectors.toList());
    }


    @Override
    public void save(Specialty specialty) {
        if (specialty.getId() == null) {
            specialty.setId(nextId++);
        }
        specialtyMap.put(specialty.getId(), specialty);
    }

    @Override
    public void delete(String specialtyId) {
        specialtyMap.remove(specialtyId);
    }
}
