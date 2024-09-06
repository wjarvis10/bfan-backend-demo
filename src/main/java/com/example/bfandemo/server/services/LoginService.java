package com.example.bfandemo.server.services;

import com.example.bfandemo.authenticationData.Authentication;
import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.userData.AuthErrorException;
import com.example.bfandemo.userData.PasswordHashing;
import com.example.bfandemo.userData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  private ServerState serverState;

  private Authentication searchByEmail(String email) {
    for (User user : this.serverState.usersDatabase.fullData.values()){
      if (user.email.equals(email)){
        return new Authentication("processing", user);
      }
    }
    return new Authentication("denied", new AuthErrorException("Input Email could not be found."));
  }

  private Authentication verifyPassword(Authentication authentication, String password) {
    String usersPassword = authentication.user.password;
    String hashedInputPassword = PasswordHashing.hashPassword(password);
    if (password.equals(usersPassword)) {
      authentication.status = "success";
    } else {
      authentication.status = "denied";
      authentication.authErrorException = new AuthErrorException("Password did not match.");
    }
    return authentication;
  }

  public Authentication authenticate(String email, String password) {
    // verify email
    // input: String email
    // output: Authentication Object

    Authentication authentication = searchByEmail(email);
    System.out.println("Auth after email check: " + authentication.status);
    // verify the password
    // input Authentication Object
    // output Authentication Object

    if (authentication.status.equals("processing")) {
      verifyPassword(authentication, password);
    }



    return authentication;
  }
}
