package com.example.bfandemo.server.requestInfo;

public class DecisionInfo {

  public String adminAuthToken;
  public String regRequestId;
  public String decision;

  public DecisionInfo(String adminAuthToken, String regRequestId, String decision) {
    this.adminAuthToken = adminAuthToken;
    this.regRequestId = regRequestId;
    this.decision = decision;
  }

}
