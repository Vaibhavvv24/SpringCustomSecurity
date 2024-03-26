package com.example.springsecurity.event;

import com.example.springsecurity.entity.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class Registrationevent extends ApplicationEvent {

    private User user;
    private String appurl;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public Registrationevent(User user, String appUrl) {
        super(user);
        this.user=user;
        this.appurl=appUrl;
    }
}
