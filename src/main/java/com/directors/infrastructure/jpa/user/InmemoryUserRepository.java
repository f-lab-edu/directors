package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InmemoryUserRepository implements UserRepository {
    // 더미 데이터 사용
    private final Map<String, User> userMap = new HashMap<>() {{
    }};

    @Override
    public Optional<User> findByIdAndUserStatus(String id, UserStatus userStatus) {
        var user = userMap.get(id);
        return Optional.ofNullable(user).filter(u -> u.getUserStatus().equals(userStatus));
    }

    @Override
    public Optional<User> find(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public void save(User user) {
        userMap.put(user.getUserId(), user);
    }
}
