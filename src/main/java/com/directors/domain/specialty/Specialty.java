package com.directors.domain.specialty;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specialty")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Specialty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SpecialtyInfo specialtyInfo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSpecialtyInfo(SpecialtyProperty property, String description) {
        this.specialtyInfo = new SpecialtyInfo(property, description);
    }
}
