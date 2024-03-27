package com.example.springsecurity.service;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerfiyToken;
import com.example.springsecurity.model.UserModel;

import java.util.Optional;

public interface UserService {
    public VerfiyToken generateNewToken(String oldtoken);

    public User registerUser(UserModel um);

    public void saveVerifyToken(String token, User user);

    String verifyValidVerificationToken(String token);

    User findUserbyEmail(String email);

    void createResetToken(User user, String pwdresettoken);

    String verifyValidPasswordResetToken(String pwdresetToken);

    Optional<User> getUserBypwdToken(String pwdtoken);

    void changePassword(User user, String newpwd);

    boolean checkOldPwd(User user, String oldpwd);
}
