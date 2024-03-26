package com.example.springsecurity.service;

import com.example.springsecurity.Repository.PasswordRepo;
import com.example.springsecurity.Repository.UserRepo;
import com.example.springsecurity.Repository.VerifyRepo;
import com.example.springsecurity.entity.PasswordResetToken;
import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerfiyToken;
import com.example.springsecurity.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VerifyRepo verifyRepo;
    @Autowired
    private PasswordRepo passwordRepo;

    @Autowired      //acts like constructor injection
    private PasswordEncoder passwordEncoder;

    @Override
    public VerfiyToken generateNewToken(String oldtoken) {
        VerfiyToken vt=verifyRepo.findByToken(oldtoken);
        vt.setToken(UUID.randomUUID().toString());
        verifyRepo.save(vt);

        return vt;
    }

    public User registerUser(UserModel um){
        User user=new User();
        user.setEmail(um.getEmail());
        user.setFirstName(um.getFirstName());
        user.setLastName(um.getLastName());
        user.setRole("USER");
        user.setPwd(passwordEncoder.encode(um.getPwd())); //plain text till now and encoded by crypt just now

        userRepo.save(user);
        return user;
    }

    @Override
    public void saveVerifyToken(String token, User user) {
        VerfiyToken verfiyToken=new VerfiyToken(user,token);
        verifyRepo.save(verfiyToken);
    }

    @Override
    public String verifyValidVerificationToken(String token) {
        VerfiyToken verfiyToken=verifyRepo.findByToken(token);
        if(verfiyToken==null){
            return "invalid";
        }
        User user=verfiyToken.getUser();
        Calendar cal=Calendar.getInstance();
        if(verfiyToken.getExpiry().getTime()-cal.getTime().getTime()<=0){
            verifyRepo.delete(verfiyToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public User findUserbyEmail(String email) {
        User user=userRepo.findByEmail(email);
        return user;
    }

    @Override
    public void createResetToken(User user, String pwdresettoken) {
        PasswordResetToken passwordResetToken=new PasswordResetToken(user,pwdresettoken);
        passwordRepo.save(passwordResetToken);
    }

    @Override
    public String verifyValidPasswordResetToken(String pwdresetToken) {
        PasswordResetToken pToken=passwordRepo.findByToken(pwdresetToken);
        if(pToken==null){
            return "invalid";
        }
        User user=pToken.getUser();
        Calendar cal=Calendar.getInstance();
        if(pToken.getExpirationTime().getTime()-cal.getTime().getTime()<=0){
            passwordRepo.delete(pToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserBypwdToken(String pwdtoken) {
        return Optional.ofNullable(passwordRepo.findByToken(pwdtoken).getUser());
    }

    @Override
    public void changePassword(User user, String newpwd) {
        user.setPwd(passwordEncoder.encode(newpwd));
        userRepo.save(user);
    }
}
