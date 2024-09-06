package com.example.bfandemo.server.requestInfo;


public class RegisterInfo {

  public String firstName;
  public String lastName;
  public String type;
  public String email;
  public String position;
  public String classYear;
  public String time;

  public RegisterInfo() {
    this.firstName = "";
    this.lastName = "";
    this.type = "";
    this.email = "";
    this.position = "";
    this.classYear = "";
    this.time = "";
  }

  public RegisterInfo(String firstName, String lastName, String type, String email, String position, String classYear, String time) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.type = type;
    this.email = email;
    this.position = position;
    this.classYear = classYear;
    this.time = time;
  }

}
