package com.directors.infrastructure.jpa.specialty;

import com.directors.domain.specialty.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSpecialtyRepository extends JpaRepository<Specialty, Long> {
    List<Specialty> findByUserId(String userId);
}