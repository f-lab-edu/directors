package com.directors.presentation.user.response;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchDirectorResponse {
    String id;
    String name;
    List<Specialty> specialtyList;

    private SearchDirectorResponse(String id, String name, List<Specialty> specialtyList) {
        this.id = id;
        this.name = name;
        this.specialtyList = specialtyList;
    }

    public static SearchDirectorResponse from(User user) {
        return new SearchDirectorResponse(user.getId(), user.getNickname(), user.getSpecialtyList());
    }
}
