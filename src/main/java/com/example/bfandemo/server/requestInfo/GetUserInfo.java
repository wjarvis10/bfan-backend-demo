package com.example.bfandemo.server.requestInfo;

public class GetUserInfo {

  public String userId;
  public String processId;

  public GetUserInfo(String userId, String processId) {
    this.userId = userId;
    this.processId = processId;
  }
}
