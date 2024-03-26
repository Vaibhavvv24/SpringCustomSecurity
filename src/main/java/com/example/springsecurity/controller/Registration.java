package com.example.springsecurity.controller;

import com.example.springsecurity.entity.PasswordResetToken;
import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerfiyToken;
import com.example.springsecurity.event.Registrationevent;
import com.example.springsecurity.model.PasswordModel;
import com.example.springsecurity.model.UserModel;
import com.example.springsecurity.service.UserService;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class Registration {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/hello")
    public String sab(){
        return "hehrj ifl hello";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel um, final HttpServletRequest servletRequest){
        User user=userService.registerUser(um);
        applicationEventPublisher.publishEvent(new Registrationevent(user,
                applicationUrl(servletRequest)
                ));
        return "Success";
    }
    @GetMapping("/verifyRegistration")
    public String verify(@RequestParam("token") String token){
        String result=userService.verifyValidVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified successfully";
        }
        return "bad request";

    }
    @GetMapping("/resendVerificationToken")
    public String resendverificationToken(@RequestParam("token") String oldtoken,HttpServletRequest request){
        VerfiyToken token=userService.generateNewToken(oldtoken);

        User user=token.getUser();
        resendverificationTokenemail(user,applicationUrl(request),token);
        return "Verification token sent again";

    }
    @PostMapping("/resetpwd")
    public String resetPassword(@RequestBody PasswordModel pm,HttpServletRequest req){
        User user=userService.findUserbyEmail(pm.getEmail());
        String url="";
        if(user!=null){
            String pwdresettoken= UUID.randomUUID().toString();
            userService.createResetToken(user,pwdresettoken);
            url=passwordverificationTokenEmail(user,applicationUrl(req),pwdresettoken);
        }
        return url;
    }
    @PostMapping("/savepwd")
    public String savePassword(@RequestParam("token") String pwdresetToken,@RequestBody PasswordModel pm){
        String result=userService.verifyValidPasswordResetToken(pwdresetToken);
        if(!result.equalsIgnoreCase("valid")){
            return "invalid token";
        }
        Optional<User> user=userService.getUserBypwdToken(pwdresetToken);
        if(user.isPresent()){
            userService.changePassword(user.get(),pm.getNewpwd());
            return "Password reset successful";
        }
        else {
            return "Invalid";
        }


    }

    private String passwordverificationTokenEmail(User user, String s, String pwdresettoken) {
        String url= s+"/savepwd?token="+ pwdresettoken;
        System.out.println(url);
        return url;
    }


    private void resendverificationTokenemail(User user, String s,VerfiyToken token) {
        String url= s+"/verifyRegistration?token="+ token.getToken();
        System.out.println(url);
    }

    private String applicationUrl(HttpServletRequest servletRequest) {
        return "http://"+ servletRequest.getServerName()+":"+servletRequest.getServerPort()+servletRequest.getContextPath();
    }

}
