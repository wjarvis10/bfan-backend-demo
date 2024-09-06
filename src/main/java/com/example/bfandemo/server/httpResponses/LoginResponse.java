package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;
import java.util.HashMap;

public class LoginResponse {
  public boolean success;
  public String authToken;
  public User user;
  public HashMap<Integer, User> usersDatabase;


  public LoginResponse(boolean success, String authToken, User user, HashMap<Integer, User> usersDatabase) {
    this.success = success;
    this.authToken = authToken;
    this.user = user;
    this.usersDatabase = usersDatabase;
  }

}
