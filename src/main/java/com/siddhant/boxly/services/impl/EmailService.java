package com.siddhant.boxly.services.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    public void sendVerificationEmail(String email,String firstName,String token){
        Context context = new Context();
        context.setVariable("token",token);
        context.setVariable("userName",firstName);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"UTF-8");

        try{
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject("Registration Confirmation");
            String content = templateEngine.process("AccountConfirmation",context);
            mimeMessageHelper.setText(content,true);
            mailSender.send(mimeMessage);
        }catch (Exception e){

        }

    }

}
