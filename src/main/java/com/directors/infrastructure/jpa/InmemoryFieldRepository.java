package com.directors.infrastructure.jpa;

import com.directors.domain.field.Field;
import com.directors.domain.field.FieldRepository;

import java.util.HashMap;
import java.util.Map;

public class InmemoryFieldRepository implements FieldRepository {

    private final Map<String, Field> fieldMap = new HashMap<>();
    private long nextId = 1; // 다음 id 값

    @Override
    public Field findFieldByFieldId(String fieldId) {
        Field field = fieldMap.get(fieldId);
        return field != null ? field : null;
    }

    @Override
    public void saveField(Field field) {
        if (field.getId() == null) {
            field.setId(String.valueOf(nextId++));
        }
        fieldMap.put(field.getId(), field);
    }

    @Override
    public void deleteField(String fieldId) {
        fieldMap.remove(fieldId);
    }
}
