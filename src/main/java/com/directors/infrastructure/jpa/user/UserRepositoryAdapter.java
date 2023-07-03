package com.directors.infrastructure.jpa.user;

import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.directors.domain.schedule.QSchedule.schedule;
import static com.directors.domain.specialty.QSpecialty.specialty;
import static com.directors.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus) {
        return userRepository.findByIdAndUserStatus(id, userStatus);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findWithSearchConditions(List<Long> regionIds, boolean hasSchedule, String searchText, SpecialtyProperty property, int offset, int limit) {
        List<String> userIds = queryFactory.select(user.id)
                .from(user)
                .join(user.specialtyList, specialty)
                .join(user.scheduleList, schedule)
                .where(regionExpression(regionIds)
                        .and(user.userStatus.eq(UserStatus.JOINED))
                        .and(hasScheduleExpression(hasSchedule))
                        .and(containExpression(user.nickname, searchText)
                                .or(containExpression(user.name, searchText))
                                .or(containExpression(specialty.specialtyInfo.description, searchText))
                        )
                        .and(propertyExpression(property))
                )
                .distinct()
            .offset(offset)
            .limit(limit)
            .fetch();

        return queryFactory.selectFrom(user)
                .join(user.specialtyList, specialty).fetchJoin()
                .where(user.id.in(userIds))
                .fetch();

    }

    @Override
    public void saveAll(List<User> user) {
        userRepository.saveAll(user);
    }

    private BooleanExpression regionExpression(List<Long> regionIds) {
        if (regionIds == null || regionIds.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return user.region.id.in(regionIds);
    }

    private BooleanExpression hasScheduleExpression(boolean hasSchedule) {
        if (!hasSchedule) {
            return Expressions.asBoolean(true).isTrue();
        }
        return schedule.startTime.after(LocalDateTime.now()).and(schedule.status.eq(ScheduleStatus.OPENED));
    }

    private BooleanExpression containExpression(final StringPath stringPath, final String searchText) {
        if (searchText != null) {
            return stringPath.likeIgnoreCase("%" + searchText + "%");
        }
        return Expressions.asBoolean(true).isTrue();
    }

    private BooleanExpression propertyExpression(SpecialtyProperty property) {
        if (property != null) {
            return specialty.specialtyInfo.property.eq(property);
        } else {
            return Expressions.asBoolean(true).isTrue();
        }
    }
}
