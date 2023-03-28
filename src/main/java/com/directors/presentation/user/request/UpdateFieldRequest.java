package com.directors.presentation.user.request;

import com.directors.domain.field.Field;
import com.directors.domain.field.FieldProperty;

public record UpdateFieldRequest(
        String id,
        String property,
        String description,
        String userId
) {
    public Field toEntity() {
        return Field.builder()
                .id(id)
                .property(FieldProperty.fromValue(property))
                .description(description)
                .userId(userId)
                .build();
    }
}
