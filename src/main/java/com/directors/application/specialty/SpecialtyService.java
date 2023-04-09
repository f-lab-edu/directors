package com.directors.application.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Transactional
    public void createSpecialty(Specialty specialty, String userIdByToken) {
        specialty.setUserId(userIdByToken);
        specialtyRepository.saveSpecialty(specialty);
    }

    @Transactional
    public void updateSpecialty(Specialty requestSpecialty) {
        var specialtyByFieldId = specialtyRepository.findSpecialtyByFieldId(requestSpecialty.getId());
        var specialty = specialtyByFieldId.orElseThrow(() -> new NoSuchElementException());

        specialty.updateSpecialtyInfo(requestSpecialty.getProperty(), requestSpecialty.getDescription());

        specialtyRepository.saveSpecialty(specialty);
    }

    @Transactional
    public void deleteSpecialty(String specialty) {
        specialtyRepository.deleteSpecialty(specialty);
    }
}
