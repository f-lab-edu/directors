package com.directors.domain.specialty;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpecialtyInfo {
    @Enumerated(EnumType.STRING)
    SpecialtyProperty property;
    String description;

    public SpecialtyInfoValue getSpecialtyInfoValue() {
        return new SpecialtyInfoValue(this.getProperty().getValue(), this.description);
    }

    @Getter
    public static class SpecialtyInfoValue {
        String property;
        String description;

        public SpecialtyInfoValue(String property, String description) {
            this.property = property;
            this.description = description;
        }
    }
}
