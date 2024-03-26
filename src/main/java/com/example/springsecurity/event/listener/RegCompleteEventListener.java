package com.example.springsecurity.event.listener;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.event.Registrationevent;
import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class RegCompleteEventListener implements ApplicationListener<Registrationevent> {
@Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(Registrationevent event) {
        User user=event.getUser();
        String token= UUID.randomUUID().toString();
        userService.saveVerifyToken(token,user);

        String url= event.getAppurl()+"/verifyRegistration?token="+token;
        System.out.println("Click on the url");
        System.out.println(url);
    }
}
