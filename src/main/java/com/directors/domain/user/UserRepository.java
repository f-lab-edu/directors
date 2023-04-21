package com.directors.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus);

    Optional<User> findById(String id);

    User save(User user);
}
