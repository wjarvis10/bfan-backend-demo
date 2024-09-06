package com.example.bfandemo.authenticationData;

import com.example.bfandemo.userData.AuthErrorException;
import com.example.bfandemo.userData.User;

public class Authentication {

  //  - String status: processing / success / denied
  //  - User user (if status = denied then user = null)
  //  - LoginErrorException loginErrorException

  public String status;
  public User user;
  public AuthErrorException authErrorException;

  public Authentication(String status, User user) {
    this.status = status;
    this.user = user;
  }

  public Authentication(String status, AuthErrorException authErrorException) {
    this.status = status;
    this.authErrorException = authErrorException;
  }

  public Authentication(String status, User user, AuthErrorException authErrorException) {
    this.status = status;
    this.user = user;
    this.authErrorException = authErrorException;
  }
}
