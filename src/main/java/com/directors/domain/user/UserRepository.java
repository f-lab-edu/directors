package com.directors.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus);

    Optional<User> findById(String id);

    User save(User user);

    List<User> findWithSearchConditions(List<Long> regionIds, String searchText, String property, int offset, int limit);
}
