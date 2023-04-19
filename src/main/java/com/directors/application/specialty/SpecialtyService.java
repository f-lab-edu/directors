package com.directors.application.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Transactional
    public void createSpecialty(Specialty specialty, String userIdByToken) {
        specialty.setUserId(userIdByToken);
        specialtyRepository.save(specialty);
    }

    @Transactional
    public void updateSpecialty(Specialty requestSpecialty) {
        var specialtyByFieldId = specialtyRepository.findByFieldId(requestSpecialty.getId());
        var specialty = specialtyByFieldId.orElseThrow(NoSuchElementException::new);

        specialty.setSpecialtyInfo(requestSpecialty.getSpecialtyInfo().property(), requestSpecialty.getSpecialtyInfo().description());

        specialtyRepository.save(specialty);
    }

    @Transactional
    public void deleteSpecialty(String specialty) {
        specialtyRepository.delete(specialty);
    }
}
