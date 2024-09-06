package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;

public class OtherUserInfoResponse {

  public boolean success;
  public User otherUser;

  public OtherUserInfoResponse(boolean success, User user) {
    this.success = success;
    this.otherUser = user;
  }

}
