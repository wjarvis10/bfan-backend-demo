package com.example.bfandemo.server.controllers;

import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.PostResponse;
import com.example.bfandemo.server.requestInfo.ResetPasswordInfo;
import com.example.bfandemo.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "resetPassword")
@CrossOrigin(origins = "http://bfanetwork.com")
public class ResetPasswordController {

  @Autowired
  private ServerState serverState;

  @PostMapping
  public ResponseEntity resetPassword(@RequestBody ResetPasswordInfo resetPasswordInfo) {
    System.out.println(resetPasswordInfo);
    String resetIdString = resetPasswordInfo.resetId;
    Integer resetId = Integer.parseInt(resetIdString);
//    System.out.println("Reset Password Controller recieved resetId: " + resetId);
    User user = serverState.usersDatabase.fullData.get(serverState.resetsRequested.get(resetId));
    serverState.resetsRequested.remove(resetId);
    System.out.println(user.name + " is reseting their password");
    String newPassword = resetPasswordInfo.newPassword;
    System.out.println("newPassword: " + newPassword);
    serverState.usersDatabase.editUser("password", newPassword, user.userId);

    return ResponseEntity.ok(new PostResponse(true));
//    } else {
//      return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Email not found");
//    }

  }
}
