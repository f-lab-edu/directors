package com.directors.presentation.user.response;

import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchDirectorResponse {
    String id;
    String name;
    List<SpecialtyInfo.SpecialtyInfoValue> specialtyList;

    private SearchDirectorResponse(String id, String name, List<SpecialtyInfo.SpecialtyInfoValue> specialtyList) {
        this.id = id;
        this.name = name;
        this.specialtyList = specialtyList;
    }

    public static SearchDirectorResponse from(User user) {
        List<SpecialtyInfo.SpecialtyInfoValue> responses = user.getSpecialtyList().stream().map(
            s -> s.getSpecialtyInfo().getSpecialtyInfoValue()
        ).collect(Collectors.toList());

        return new SearchDirectorResponse(user.getId(), user.getNickname(), responses);
    }
}
