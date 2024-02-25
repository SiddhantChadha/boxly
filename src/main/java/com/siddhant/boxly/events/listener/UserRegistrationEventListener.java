package com.siddhant.boxly.events.listener;

import com.siddhant.boxly.events.UserRegistrationEvent;
import com.siddhant.boxly.services.VerificationTokenService;
import com.siddhant.boxly.services.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener {

    private EmailService emailService;
    private VerificationTokenService verificationTokenService;

    @Autowired
    public UserRegistrationEventListener(EmailService emailService, VerificationTokenService verificationTokenService){
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
    }


    @EventListener(UserRegistrationEvent.class)
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent){
          String verificationToken = verificationTokenService.createToken(userRegistrationEvent.getEmail());
//          emailService.sendVerificationEmail(userRegistrationEvent.getEmail(),verificationToken);
    }

}
