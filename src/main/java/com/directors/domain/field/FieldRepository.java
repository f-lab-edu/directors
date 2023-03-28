package com.directors.domain.field;

public interface FieldRepository {
    Field findFieldByFieldId(String fieldId);

    void saveField(Field field);

    void deleteField(String fieldId);

}
