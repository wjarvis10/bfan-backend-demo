package com.example.bfandemo.authenticationData;

import com.example.bfandemo.userData.User;
import java.time.Instant;
import java.util.Random;

public class AuthenticatedUser {

  private User user;
  private Instant createdAt;


  public AuthenticatedUser(User user) {
    this.user = user;
    this.createdAt = Instant.now();
  }

  public static String generateAuthToken() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder token = new StringBuilder(10);
    Random random = new Random();

    for (int i = 0; i < 10; i++) {
      int index = random.nextInt(characters.length());
      token.append(characters.charAt(index));
    }

    return token.toString();
  }

  // getter for authToken
  public User getUser() { return this.user; }
  // setter for createdAt
  public void setCreatedAt(Instant instant) {
    this.createdAt = instant;
  }
  // getter for createdAt
  public Instant getCreatedAt() {
    return this.createdAt;
  }




}
