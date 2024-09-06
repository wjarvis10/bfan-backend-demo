package com.example.bfandemo.server.controllers;

import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.PostResponse;
import com.example.bfandemo.server.requestInfo.ForgotPasswordInfo;
import com.example.bfandemo.server.services.EmailService;
import com.example.bfandemo.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "forgotPassword")
@CrossOrigin(origins = "http://bfanetwork.com")
public class ForgotPasswordController {

  @Autowired
  private ServerState serverState;

  private final EmailService emailService;

  public ForgotPasswordController(EmailService emailService) {this.emailService = emailService;}

  @PostMapping
  public ResponseEntity forgotPassword(@RequestBody ForgotPasswordInfo forgotPasswordInfo) {
    System.out.println(forgotPasswordInfo);
    String email = forgotPasswordInfo.email;
    System.out.println("Forgot Password Controller recieved email: " + email);

    Boolean success = false;
    User currentUser = null;

    for (User user : serverState.usersDatabase.fullData.values()) {
      if (user.email.equals(email)) {
        success = true;
        currentUser = user;
        break;
      }
    }

    if (success) {
      String recipientEmail = email;
      int userId = currentUser.userId;
      int resetId = serverState.usersDatabase.generateId();
      serverState.resetsRequested.put(resetId, userId);
      String resetUrl = "http://bfanetwork.com/resetPassword?resetId=" + resetId;
      String body = "Click the link below to reset your password:\n" + resetUrl;
      String subject = "Password Reset Request";

      emailService.sendEmail(email, subject, body);

      return ResponseEntity.ok(new PostResponse(true));
    } else {
      return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Email not found");
    }

  }
}
