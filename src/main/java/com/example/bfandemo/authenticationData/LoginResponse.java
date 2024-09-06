package com.example.bfandemo.authenticationData;

public class LoginResponse {
  public boolean success;
  public String authToken;

  public LoginResponse(boolean success, String authToken) {
    this.success = success;
    this.authToken = authToken;
  }

}
