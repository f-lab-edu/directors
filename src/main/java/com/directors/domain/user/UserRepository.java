package com.directors.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus);

    Optional<User> find(String id);

    void save(User user);
}
