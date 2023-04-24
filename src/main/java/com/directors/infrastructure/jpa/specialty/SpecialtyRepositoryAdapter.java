package com.directors.infrastructure.jpa.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpecialtyRepositoryAdapter implements SpecialtyRepository {
    private final JpaSpecialtyRepository specialtyRepository;

    @Override
    public Optional<Specialty> findById(Long specialtyId) {
        return specialtyRepository.findById(specialtyId);
    }

    @Override
    public List<Specialty> findByUserId(String userId) {
        return specialtyRepository.findByUserId(userId);
    }

    @Override
    public void save(Specialty specialty) {
        specialtyRepository.save(specialty);
    }

    @Override
    public void delete(Long specialtyId) {
        specialtyRepository.deleteById(specialtyId);
    }
}
