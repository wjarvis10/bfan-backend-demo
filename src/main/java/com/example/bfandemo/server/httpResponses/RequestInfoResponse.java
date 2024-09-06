package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.server.util.RegistrationRequest;

public class RequestInfoResponse {

  public boolean success;
  public Integer authToken;
  public RegistrationRequest registrationRequest;

  public RequestInfoResponse(boolean success, Integer authToken, RegistrationRequest registrationRequest) {
    this.success = success;
    this.authToken = authToken;
    this.registrationRequest = registrationRequest;
  }
}
