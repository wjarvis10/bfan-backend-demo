package com.example.bfandemo.server.requestInfo;

public class FinishRequestInfo {

  public String userId;
  public String processId;
  //public HashMap<String, String> profileChanges;
  public String profileChanges;

  public FinishRequestInfo(String userId, String processId, String profileChanges) {
    this.userId = userId;
    this.processId = processId;
    this.profileChanges = profileChanges;
  }
}
