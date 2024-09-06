package com.example.bfandemo.server.util;

import com.example.bfandemo.userData.User;

public class RegistrationRequest {

  public String time;
  public User user;

  public RegistrationRequest(String time, User user) {
    this.time = time;
    this.user = user;
  }
}
