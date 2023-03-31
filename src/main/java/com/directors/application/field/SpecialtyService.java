package com.directors.application.field;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public void createSpecialty(Specialty specialty, String userIdByToken) {
        specialty.setUserId(userIdByToken);
        specialtyRepository.saveSpecialty(specialty);
    }

    public void updateSpecialty(Specialty requestSpecialty) {
        Specialty specialty = specialtyRepository.findSpecialtyByFieldId(requestSpecialty.getId());

        specialty.updateSpecialtyInfo(requestSpecialty.getProperty(), requestSpecialty.getDescription());

        specialtyRepository.saveSpecialty(specialty);
    }

    public void deleteSpecialty(String specialty) {
        specialtyRepository.deleteSpecialty(specialty);
    }
}
