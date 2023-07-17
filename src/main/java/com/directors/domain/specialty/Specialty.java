package com.directors.domain.specialty;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specialty")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Specialty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    SpecialtyProperty property;

    String description;

    @Builder
    public Specialty(SpecialtyProperty property, String description, User user) {
        this.property = property;
        this.description = description;
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
