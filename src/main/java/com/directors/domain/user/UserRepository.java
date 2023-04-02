package com.directors.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByIdAndUserStatus(String id, UserStatus userStatus);

    void saveUser(User user);
}
