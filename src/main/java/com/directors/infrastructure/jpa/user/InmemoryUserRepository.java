package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InmemoryUserRepository implements UserRepository {
    // 더미 데이터 사용
    private final Map<String, User> userMap = new HashMap<>() {{
        put("song0229", new User("song0229", "12341234", "송은석", "song0229",
                "thddmstjrwkd@naver.com", "010-7702-1045", "", 0));
        put("park1234", new User("park1234", "park1234", "박도현", "park1234",
                "park1234@naver.com", "010-7702-1045", "", 0));
        put("eunseok999", new User("eunseok999", "eunseok999", "은석송", "eunseok999",
                "eunseok999@naver.com", "010-7702-1045", "", 0));
        put("dohyun777", new User("dohyun777", "dohyun777", "도현박", "dohyun777",
                "dohyun777@naver.com", "010-7702-1045", "", 0));
    }};

    @Override
    public User findUserById(String id) {
        return userMap.get(id);
    }

    @Override
    public void saveUser(User user) {
        userMap.put(user.getUserId(), user);
    }
}
