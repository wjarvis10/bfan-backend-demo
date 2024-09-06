package com.example.bfandemo.server.requestInfo;


public class LoginInfo {

  public String email;
  public String password;

  public LoginInfo() {
    this.email = "";
    this.password = "";
  }

  public LoginInfo(String email, String password) {
    this.email = email;
    this.password = password ;
  }

}
