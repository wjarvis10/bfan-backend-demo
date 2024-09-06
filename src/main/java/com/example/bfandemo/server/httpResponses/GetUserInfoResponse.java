package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;

public class GetUserInfoResponse {

  public boolean success;
  public User user;

  public GetUserInfoResponse(boolean success, User user) {
    this.success = success;
    this.user = user;
  }

}
