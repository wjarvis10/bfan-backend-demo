package com.example.bfandemo.server.requestInfo;

public class SetPasswordInfo {

  public String userId;
  public String processId;
  public String newPassword;

  public SetPasswordInfo(String userId, String processId, String newPassword) {
    this.userId = userId;
    this.processId = processId;
    this.newPassword = newPassword;
  }
}
