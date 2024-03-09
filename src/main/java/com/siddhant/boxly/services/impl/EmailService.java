package com.siddhant.boxly.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public EmailService(MailSender mailSender){
        this.mailSender = mailSender;
    }


    public void sendVerificationEmail(String email,String token){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Registration Confirmation");
        simpleMailMessage.setText(token);
        mailSender.send(simpleMailMessage);
    }

}
