package com.example.bfandemo.server.controllers;


//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
//public class LoginController {
//
//  @GetMapping("/login")
//  public String loginMessage() {
//    return "Welcome to my BFAN login endpoint!";
//  }
//}

import com.example.bfandemo.authenticationData.AuthenticatedUser;
import com.example.bfandemo.authenticationData.Authentication;
import com.example.bfandemo.server.requestInfo.LoginInfo;
import com.example.bfandemo.server.httpResponses.LoginResponse;
import com.example.bfandemo.server.services.LoginService;
import com.example.bfandemo.server.ServerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
@CrossOrigin(origins = "http://bfanetwork.com")
//@CrossOrigin(origins = "http://localhost:5173/")
public class LoginController {

  @Autowired
  private ServerState serverState;
  @Autowired
  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }
  @PostMapping
  public ResponseEntity HandleLogin(@RequestBody LoginInfo loginInfo) {
    System.out.println(loginInfo);
    String email = loginInfo.email;
    String password = loginInfo.password;
    System.out.println("Login Controller recieved email: " + email + ", password: " + password);

    Authentication authentication = loginService.authenticate(email, password);

    if (authentication.status.equals("success")) {
      // - need to create a authenticationToken return it to the frontend
      // - need to create an authenticatedUser with the authenticationToken + user info and add it
      //   to the list of authenticatedUsers in state

      String authToken = AuthenticatedUser.generateAuthToken();
      this.serverState.authenticatedUsers.put(authToken, new AuthenticatedUser(authentication.user));

      return (ResponseEntity.ok(new LoginResponse(true, authToken, authentication.user,this.serverState.usersDatabase.fullData)));

    } else {
      return(ResponseEntity.status(HttpStatusCode.valueOf(401)).body(authentication.authErrorException.getMessage()));
    }

  }
}
