package com.directors.domain.specialty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Specialty {
    private String id;
    private SpecialtyProperty property;
    private String description;
    private String userId;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void updateSpecialtyInfo(SpecialtyProperty property, String description) {
        this.property = property;
        this.description = description;
    }
}
