package com.directors.presentation.field.request;

import com.directors.domain.field.Field;
import com.directors.domain.field.FieldProperty;

public record CreateFieldRequest(
        String property,
        String description
) {
    public Field toEntity() {
        return Field.builder()
                .property(FieldProperty.fromValue(property))
                .description(description)
                .build();
    }
}
