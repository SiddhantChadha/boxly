package com.siddhant.boxly.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {
    private String email;

    public UserRegistrationEvent(String email) {
        super(email);
        this.email = email;
    }
}
