package com.directors.application.specialty;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.domain.specialty.exception.NoSuchSpecialtyException;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.presentation.specialty.request.CreateSpecialtyRequest;
import com.directors.presentation.specialty.request.UpdateSpecialtyRequest;
import com.directors.presentation.specialty.response.CreateSpecialtyResponse;
import com.directors.presentation.specialty.response.UpdateSpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateSpecialtyResponse createSpecialty(CreateSpecialtyRequest request, String userId) {
        var user = userRepository.findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));

        Specialty specialty = Specialty.builder()
                .property(SpecialtyProperty.fromValue(request.specialtyProperty()))
                .description(request.description())
                .user(user)
                .build();

        return CreateSpecialtyResponse.from(specialtyRepository.save(specialty));
    }

    @Transactional
    public UpdateSpecialtyResponse updateSpecialty(UpdateSpecialtyRequest request) {
        var specialty = specialtyRepository.findById(request.id())
                .orElseThrow(NoSuchSpecialtyException::new);

        specialty.setDescription(request.description());

        return UpdateSpecialtyResponse.from(specialty);
    }

    @Transactional
    public void deleteSpecialty(Long specialtyId) {
        var specialty = specialtyRepository
                .findById(specialtyId)
                .orElseThrow(NoSuchSpecialtyException::new);

        specialtyRepository.delete(specialty.getId());
    }
}
