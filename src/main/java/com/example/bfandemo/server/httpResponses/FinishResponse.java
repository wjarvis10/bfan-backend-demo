package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;

public class FinishResponse {

  public String authToken;
  public User user;

  public FinishResponse(String authToken, User user) {
    this.authToken = authToken;
    this.user = user;
  }

}
