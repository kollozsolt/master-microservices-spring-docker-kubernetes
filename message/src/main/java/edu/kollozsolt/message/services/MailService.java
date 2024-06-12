package edu.kollozsolt.message.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String text) {

    }

    String bodyText = """
                 Dear %s,
                 
                 Your account has been successfully created!
                 
                 If you did not create it, please contact customer support!
                 """.formatted(accountsMessageDto.name());

            message.setTo(accountsMessageDto.email());
            message.setSubject("Account created successfully!");
            message.setText(bodyText);
}
