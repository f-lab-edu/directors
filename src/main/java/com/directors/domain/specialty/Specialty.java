package com.directors.domain.specialty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Specialty {
    private String id;
    private SpecialtyInfo specialtyInfo;
    private String userId;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSpecialtyInfo(SpecialtyProperty property, String description) {
        this.specialtyInfo = new SpecialtyInfo(property, description);
    }
}
