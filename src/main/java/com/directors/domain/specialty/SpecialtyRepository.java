package com.directors.domain.specialty;

public interface SpecialtyRepository {
    Specialty findSpecialtyByFieldId(String specialtyId);

    void saveSpecialty(Specialty specialty);

    void deleteSpecialty(String specialtyId);

}
