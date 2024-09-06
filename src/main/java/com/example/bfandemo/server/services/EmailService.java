package com.example.bfandemo.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {this.mailSender = mailSender;}

  public void sendEmail(String to, String subject, String body) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("bfanetwork1@gmail.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    mailSender.send(message);
  }

}

