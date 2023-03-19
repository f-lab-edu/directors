package com.directors.domain.user;

public interface PasswordManager {
    String encryptPassword(String password);

    boolean checkPassword(String password, String hashedPassword);
}
