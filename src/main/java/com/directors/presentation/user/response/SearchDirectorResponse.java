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
    List<SpecialtyValue> specialtyList;

    private SearchDirectorResponse(String id, String name, List<SpecialtyValue> specialtyList) {
        this.id = id;
        this.name = name;
        this.specialtyList = specialtyList;
    }

    public static SearchDirectorResponse from(User user) {
        List<SpecialtyValue> specialtyValues = user.getSpecialtyList().stream().map(sp ->
            new SpecialtyValue(sp.getProperty().getValue(), sp.getDescription())
        ).collect(Collectors.toList());

        return new SearchDirectorResponse(user.getId(), user.getNickname(), specialtyValues);
    }

    @Getter
    public static class SpecialtyValue {
        String propertyValue;
        String description;

        public SpecialtyValue(String propertyValue, String description) {
            this.propertyValue = propertyValue;
            this.description = description;
        }
    }
}
