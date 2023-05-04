package com.directors.presentation.user.response;

import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.user.User;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchDirectorResponse(
        String directorId,
        String directorName,
        List<SpecialtyInfo> specialtyInfos
) {
    public static SearchDirectorResponse of(User user) {
        return SearchDirectorResponse.builder()
                .directorId(user.getId())
                .directorName(user.getName())
                .specialtyInfos(user.getSpecialtyInfoList())
                .build();
    }

}
