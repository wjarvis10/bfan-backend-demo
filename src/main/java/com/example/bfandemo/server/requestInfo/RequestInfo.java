package com.example.bfandemo.server.requestInfo;

public class RequestInfo {

  public String adminAuthToken;
  public String regRequestId;

  public RequestInfo() {
    this.adminAuthToken = "";
    this.regRequestId = "";
  }

  public RequestInfo(String adminAuthToken, String regRequestId) {
    this.adminAuthToken = adminAuthToken;
    this.regRequestId = regRequestId;
  }

}
