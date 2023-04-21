package com.directors.application.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.specialty.NoSuchSpecialtyException;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import com.directors.infrastructure.jpa.specialty.JpaSpecialtyRepository;
import com.directors.infrastructure.jpa.user.JpaUserRepository;
import com.directors.presentation.specialty.request.UpdateSpecialtyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final JpaSpecialtyRepository specialtyRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public void createSpecialty(Specialty specialty, String userIdByToken) {
        User user = userRepository.findByIdAndUserStatus(userIdByToken, UserStatus.JOINED)
                .orElseThrow(() -> {
                    throw new NoSuchUserException(userIdByToken);
                });
        specialty.setUser(user);

        specialtyRepository.save(specialty);
    }

    @Transactional
    public void updateSpecialty(UpdateSpecialtyRequest request) {
        var specialty = specialtyRepository
                .findById(request.id())
                .orElseThrow(NoSuchSpecialtyException::new);
        specialty.setSpecialtyInfo(SpecialtyProperty.fromValue(request.property()), request.description());

        specialtyRepository.save(specialty);
    }

    @Transactional
    public void deleteSpecialty(Long specialtyId) {
        var specialty = specialtyRepository
                .findById(specialtyId)
                .orElseThrow(NoSuchSpecialtyException::new);

        specialtyRepository.delete(specialty);
    }
}
