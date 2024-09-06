package com.example.bfandemo.server.controllers;

import com.example.bfandemo.authenticationData.AuthenticatedUser;
import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.FinishResponse;
import com.example.bfandemo.server.httpResponses.GetUserInfoResponse;
import com.example.bfandemo.server.httpResponses.PostResponse;
import com.example.bfandemo.server.requestInfo.FinishRequestInfo;
import com.example.bfandemo.server.requestInfo.GetUserInfo;
import com.example.bfandemo.server.requestInfo.SetPasswordInfo;
import com.example.bfandemo.userData.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "createAccount")
@CrossOrigin(origins = "http://bfanetwork.com")
public class CreateAccountController {

  @Autowired
  private ServerState serverState;


  private boolean authenticate(String userId, String processId) {
    Integer userIdInt = Integer.parseInt(userId);
    Integer processIdInt = Integer.parseInt(processId);
    Boolean processAuth = serverState.approvedRegistrationIds.contains(processIdInt);
    if (processAuth) {
      Boolean userAuth = serverState.usersDatabase.fullData.containsKey(userIdInt);
      if (userAuth) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @PostMapping("/setPassword")
  public ResponseEntity setPassword(@RequestBody SetPasswordInfo setPasswordInfo) {
    try {
      String userId = setPasswordInfo.userId;
      String processId = setPasswordInfo.processId;
      String newPassword = setPasswordInfo.newPassword;

      Boolean authStatus = authenticate(userId, processId);

      if (authStatus) {
        User user = serverState.usersDatabase.fullData.get(Integer.parseInt(userId));
        serverState.usersDatabase.editUser("password", newPassword, user.userId);
        return ResponseEntity.ok(new PostResponse(true));
      } else {
        return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid processId or userId"));
      }
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    }
  }

  @PostMapping("/getUserInfo")
  public ResponseEntity getInfo(@RequestBody GetUserInfo getUserInfo) {
    try {
      String userId = getUserInfo.userId;
      String processId = getUserInfo.processId;
      System.out.println(userId + "is hitting the getInfo endpoint");
      Boolean authStatus = authenticate(userId, processId);

      if (authStatus) {
        User user = serverState.usersDatabase.fullData.get(Integer.parseInt(userId));
        return ResponseEntity.ok(new GetUserInfoResponse(true, user));
      } else {
        return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid processId or userId"));
      }
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (NumberFormatException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    }
  }

  @PostMapping("/finish")
  public ResponseEntity finish(@RequestBody FinishRequestInfo finishRequestInfo) {
    try {
      String userId = finishRequestInfo.userId;
      String processId = finishRequestInfo.processId;
      //HashMap<String, String> profileChanges = finishRequestInfo.profileChanges;

      // Create an ObjectMapper instance
      ObjectMapper mapper = new ObjectMapper();
      // Deserialize the stringified profileChanges into a HashMap
      HashMap<String, String> deserializedProfileChanges = mapper.readValue(finishRequestInfo.profileChanges, HashMap.class);
      System.out.println(userId + " is hitting the createAccount/finish endpoint.\nProfile Changes:");
      System.out.println(deserializedProfileChanges);
      Boolean authStatus = authenticate(userId, processId);

      if (authStatus) {
        User user = serverState.usersDatabase.fullData.get(Integer.parseInt(userId));

        // update the user's account with the new inputted information
        for (String field : deserializedProfileChanges.keySet()){
          // System.out.println("field: " + field + ", new value: " + deserializedProfileChanges.get(field));
          this.serverState.usersDatabase.editUser(field, deserializedProfileChanges.get(field), user.userId);
        }

        // update filter lists with user's info
        this.serverState.usersDatabase.updateFilterLists(user.userId);


        // generate authToken for the new user's initial session
        String authToken = AuthenticatedUser.generateAuthToken();
        this.serverState.authenticatedUsers.put(authToken, new AuthenticatedUser(user));

        return ResponseEntity.ok(new FinishResponse(authToken, user));
      } else {
        return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid processId or userId"));
      }
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (NumberFormatException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (com.fasterxml.jackson.core.JsonProcessingException e ) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (ClassCastException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    }
  }

}
