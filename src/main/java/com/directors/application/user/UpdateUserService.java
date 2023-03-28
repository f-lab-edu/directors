package com.directors.application.user;

import com.directors.domain.user.PasswordManager;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.presentation.user.request.UpdateEmailRequest;
import com.directors.presentation.user.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, String userIdByToken) {
        String oldPassword = updatePasswordRequest.oldPassword();
        String newPassword = updatePasswordRequest.newPassword();

        User user = validateUser(userIdByToken);

        validatePassword(oldPassword, user);

        String encryptPassword = passwordManager.encryptPassword(newPassword);
        user.setPasswordByEncryption(encryptPassword);
    }


    public void updateEmail(UpdateEmailRequest updateEmailRequest, String userIdByToken) {
        String oldEmail = updateEmailRequest.oldEmail();
        String newEmail = updateEmailRequest.newEmail();

        User user = validateUser(userIdByToken);

        validateEmail(oldEmail, user);

        user.setEmail(newEmail);
    }

    private User validateUser(String userIdByToken) {
        User user = userRepository.findUserByIdAndUserStatus(userIdByToken, UserStatus.JOINED);
        if (user == null) {
            throw new AuthenticationFailedException(userIdByToken);
        }
        return user;
    }

    private static void validateEmail(String email, User user) {
        if (user.getEmail().equals(email)) {
            throw new AuthenticationFailedException(user.getUserId());
        }
    }

    private void validatePassword(String password, User user) {
        if (passwordManager.checkPassword(user.getPassword(), password)) {
            throw new AuthenticationFailedException(user.getUserId());
        }
    }
}
