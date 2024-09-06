package com.example.bfandemo.server.controllers;

import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.PostResponse;
import com.example.bfandemo.server.requestInfo.RegisterInfo;
import com.example.bfandemo.server.services.EmailService;
import com.example.bfandemo.server.util.RegistrationRequest;
import com.example.bfandemo.userData.Alumn;
import com.example.bfandemo.userData.Name;
import com.example.bfandemo.userData.Player;
import com.example.bfandemo.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "register")
@CrossOrigin(origins = "http://bfanetwork.com")
public class RegisterController {

  @Autowired
  private ServerState serverState;

  private final EmailService emailService;

  public RegisterController(EmailService emailService) {this.emailService = emailService;}

  private void sendNotification(User user, Integer adminAuthToken) {
    String recipientEmail = "bfanetwork1@gmail.com";
    String subject = "New Registration Request - " + user.name.firstName + " " + user.name.lastName;
    String body = "Prospective User Information \n - Name: " + user.name.firstName + " " + user.name.lastName + "\n - Type: " + user.type + "\n - Email: " + user.email + "\n - Primary Position: " + user.primaryPosition + "\n - Class Year: " + user.graduationYear + "\n\n Visit http://bfanetwork.com/admin?adminId=" + adminAuthToken + "&regReqId=" + user.userId;
    emailService.sendEmail(recipientEmail, subject, body);
  }

  @PostMapping
  public ResponseEntity register(@RequestBody RegisterInfo registerInfo) {
    System.out.println(registerInfo);
    String firstName = registerInfo.firstName;
    String lastName = registerInfo.lastName;
    String type = registerInfo.type;
    String email = registerInfo.email;
    String position = registerInfo.position;
    String classYear = registerInfo.classYear;
    String time = registerInfo.time;

    System.out.println(firstName + " " + lastName + " is registering.");

    Integer id = serverState.usersDatabase.generateId();
    Integer adminAuthToken = serverState.usersDatabase.generateId();
    if (type.equals("alumni")) {
//      User user = new Alumn(id, new Name(firstName, lastName), email, position, classYear);
      RegistrationRequest newRequest = new RegistrationRequest(time, new Alumn(id, new Name(firstName, lastName), email, position, classYear));
      serverState.registrationRequests.put(id , newRequest);
      serverState.adminAuthTokens.add(adminAuthToken);
      sendNotification(newRequest.user, adminAuthToken);
      System.out.println("new alumn registered with id: " + id + ", name: " + newRequest.user.name);
    } else if (type.equals("player")) {
      RegistrationRequest newRequest = new RegistrationRequest(time, new Player(id, new Name(firstName, lastName), email, position, classYear));
      serverState.registrationRequests.put(id , newRequest);
      serverState.adminAuthTokens.add(adminAuthToken);
      sendNotification(newRequest.user, adminAuthToken);
      System.out.println("new player registered with id: " + id + ", name: " + newRequest.user.name);
    } else {
      System.out.print("Error: registration request is for neither a player nor alumni.");
    }
    return ResponseEntity.ok(new PostResponse(true));
//    } else {
//      return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Email not found");
//    }

  }
}
