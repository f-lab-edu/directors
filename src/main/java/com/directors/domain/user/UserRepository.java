package com.directors.domain.user;

public interface UserRepository {
    User findJoinedUserById(String id);

    void saveUser(User user);
}
