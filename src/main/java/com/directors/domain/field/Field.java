package com.directors.domain.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Field {
    private String id;
    private FieldProperty property;
    private String description;
    private String userId;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void updateFieldInfo(FieldProperty property, String description) {
        this.property = property;
        this.description = description;
    }
}
