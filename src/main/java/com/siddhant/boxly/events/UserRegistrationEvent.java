package com.siddhant.boxly.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {
    private String email;
    private String firstName;

    public UserRegistrationEvent(String email, String firstName) {
        super(email);
        this.email = email;
        this.firstName = firstName;
    }
}
