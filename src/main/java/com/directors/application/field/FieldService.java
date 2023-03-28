package com.directors.application.field;

import com.directors.domain.field.Field;
import com.directors.domain.field.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;

    public void createField(Field field, String userIdByToken) {
        field.setUserId(userIdByToken);
        fieldRepository.saveField(field);
    }

    public void updateField(Field requestField) {
        Field field = fieldRepository.findFieldByFieldId(requestField.getId());

        field.updateFieldInfo(requestField.getProperty(), requestField.getDescription());

        fieldRepository.saveField(field);
    }

    public void deleteField(String fieldId) {
        fieldRepository.deleteField(fieldId);
    }
}
