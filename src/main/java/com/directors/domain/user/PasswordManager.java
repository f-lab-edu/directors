package com.directors.domain.user;

public interface PasswordManager {
    String encodePassword(String password);

    boolean checkPassword(String password, String hashedPassword);
}
