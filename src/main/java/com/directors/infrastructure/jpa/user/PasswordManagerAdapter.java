package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.PasswordManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordManagerAdapter implements PasswordManager {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
