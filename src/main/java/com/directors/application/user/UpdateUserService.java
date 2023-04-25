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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    @Transactional
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, String userIdByToken) {
        var oldPassword = updatePasswordRequest.oldPassword();
        var newPassword = updatePasswordRequest.newPassword();

        var user = validateUser(userIdByToken);
        validatePassword(oldPassword, user);

        String encryptPassword = passwordManager.encryptPassword(newPassword);

        user.setPasswordByEncryption(encryptPassword);
    }


    @Transactional
    public void updateEmail(UpdateEmailRequest updateEmailRequest, String userIdByToken) {
        String oldEmail = updateEmailRequest.oldEmail();
        String newEmail = updateEmailRequest.newEmail();

        var user = validateUser(userIdByToken);

        user.changeEmail(oldEmail, newEmail);
    }

    private User validateUser(String userId) {
        return userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new AuthenticationFailedException(userId));
    }


    private void validatePassword(String password, User user) {
        if (!passwordManager.checkPassword(password, user.getPassword())) {
            throw new AuthenticationFailedException(user.getId());
        }
    }
}
