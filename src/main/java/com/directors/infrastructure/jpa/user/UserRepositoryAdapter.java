package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository userRepository;

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
    public List<User> findWithSearchConditions(List<Long> regionIds, boolean hasSchedule, String searchText, String property, int offset, int limit) {
        return userRepository.findWithSearchConditions(regionIds, hasSchedule, searchText, property, offset, limit);
    }

    @Override
    public void saveAll(List<User> user) {
        userRepository.saveAll(user);
    }
}
