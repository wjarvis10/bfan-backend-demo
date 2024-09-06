package com.example.bfandemo.server.requestInfo;

public class ResetPasswordInfo {

  public String resetId;
  public String newPassword;

  public ResetPasswordInfo() {
    this.resetId = "";
    this.newPassword = "";
  }
  public ResetPasswordInfo(String resetId, String newPassword) {
    this.resetId = resetId;
    this.newPassword = newPassword;
  }


}
