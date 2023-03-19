package com.directors.domain.user;

public interface UserRepository {
    User findUserById(String id);

    void saveUser(User user);
}
