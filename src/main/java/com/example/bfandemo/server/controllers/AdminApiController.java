package com.example.bfandemo.server.controllers;

import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.PostResponse;
import com.example.bfandemo.server.httpResponses.RequestInfoResponse;
import com.example.bfandemo.server.requestInfo.DecisionInfo;
import com.example.bfandemo.server.requestInfo.RequestInfo;
import com.example.bfandemo.server.services.EmailService;
import com.example.bfandemo.server.util.RegistrationRequest;
import com.example.bfandemo.userData.Alumn;
import com.example.bfandemo.userData.Player;
import com.example.bfandemo.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**@CrossOrigin(origins = "http://localhost:5173")*/

@RestController
@RequestMapping(path = "admin")
@CrossOrigin(origins = "http://bfanetwork.com")
public class AdminApiController {

  @Autowired
  private ServerState serverState;

  private final EmailService emailService;

  public AdminApiController(EmailService emailService) {this.emailService = emailService;}

  private void sendNotification(String decision, User user) {
    String recipientEmail = "bfanetwork1@gmail.com";
    String subject = decision + " Registration Request - " + user.name.firstName + " " + user.name.lastName;
    String body = decision + " Prospective User\n - Name: " + user.name.firstName + " " + user.name.lastName + "\n - Type: " + user.type + "\n - Email: " + user.email + "\n - Primary Position: " + user.primaryPosition + "\n - Class Year: " + user.graduationYear;
    emailService.sendEmail(recipientEmail, subject, body);
  }

  private void sendRegisterEmail(User user, Integer processId) {
    String recipientEmail = user.email;
    String subject = "BFAN Registration Request Approved! - Finish Registering Now";
    String body = "Visit this link http://bfanetwork.com/setPassword?userId=" + user.userId + "&processId=" + processId +  " to finish registering";
    emailService.sendEmail(recipientEmail, subject, body);
  }

  private boolean authenticate(String authToken) {
    Integer authTokenInt = Integer.parseInt(authToken);
    for (Integer token : serverState.adminAuthTokens) {
      if (authTokenInt.equals(token)) {
        Boolean removed =  serverState.adminAuthTokens.remove(token);
        if (!removed) {
          System.out.println("Error: didn't remove adminAuthToken correctly.");
        }
        return true;
      }
    }
    return false;
  }

  @PostMapping("/requestInfo")
  public ResponseEntity getRequestInfo(@RequestBody RequestInfo requestInfo) {
    try {
      System.out.println("admin requestInfo endpoint hit. regRequestId: " + requestInfo.regRequestId + ", authToken: " + requestInfo.adminAuthToken);
      Boolean authStatus = authenticate(requestInfo.adminAuthToken);
      if (authStatus) {
        RegistrationRequest registrationRequest = serverState.registrationRequests.get(Integer.parseInt(requestInfo.regRequestId));
        Integer newAdminAuthToken = serverState.usersDatabase.generateId();
        serverState.adminAuthTokens.add(newAdminAuthToken);
        return ResponseEntity.ok(new RequestInfoResponse(true, newAdminAuthToken, registrationRequest));
      } else {
        return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid adminAuthToken"));
      }
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @PostMapping("/decision")
  public ResponseEntity decision(@RequestBody DecisionInfo decisionInfo) {
    try {
      System.out.println("admin decision endpoint hit. regRequestId: " + decisionInfo.regRequestId + ", authToken: " + decisionInfo.adminAuthToken + ", decision: " + decisionInfo.decision);
      Boolean authStatus = authenticate(decisionInfo.adminAuthToken);
      if (authStatus) {
        Integer id = Integer.parseInt(decisionInfo.regRequestId);
        RegistrationRequest registrationRequest = serverState.registrationRequests.remove(id);
        System.out.println(registrationRequest.user);
        User user = registrationRequest.user;
        String userType = user.type;
        String decision = decisionInfo.decision;
        if (decision.equals("approve")) {
          if (userType.equals("Player")) {
            Player newPlayer = (Player) user;
            serverState.usersDatabase.addPlayerAccount(newPlayer);
            sendNotification("Approved", newPlayer);
            Integer processId = serverState.usersDatabase.generateId();
            serverState.approvedRegistrationIds.add(processId);
            sendRegisterEmail(newPlayer, processId);
          } else if (userType.equals("Alumn")) {
            // add new alumn
            Alumn newAlumn = (Alumn) user;
            serverState.usersDatabase.addAlumnAccount(newAlumn);
            sendNotification("Approved", newAlumn);
            Integer processId = serverState.usersDatabase.generateId();
            serverState.approvedRegistrationIds.add(processId);
            sendRegisterEmail(newAlumn, processId);
          } else {
            System.out.println("Error: Request user type was neither player nor alumn");
          }
        } else if (decision.equals("decline")) {
         sendNotification("Declined", user);
        } else {
          System.out.println("Error: Request decision was neither approve nor decline");
        }
        return ResponseEntity.ok(new PostResponse(true));
      } else {
        return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid adminAuthToken"));
      }
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

}
