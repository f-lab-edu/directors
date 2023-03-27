package com.directors.domain.user;

public interface UserRepository {
    User findUserByIdAndUserStatus(String id, UserStatus userStatus);

    void saveUser(User user);
}
