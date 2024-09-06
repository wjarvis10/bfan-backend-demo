package com.example.bfandemo.server;


import com.example.bfandemo.authenticationData.AuthenticatedUser;
import com.example.bfandemo.server.util.RegistrationRequest;
import com.example.bfandemo.userData.DataBase;
import com.example.bfandemo.userData.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ServerState {
  // initialize Database instance
  // initialize authenticated users
  // initialize anything else that will handle state of backend server

  public DataBase usersDatabase;
  public HashMap<String, AuthenticatedUser> authenticatedUsers; // need to set expiration times for authenticatedUsers
  // ... methods to manage users
  public HashMap<Integer, Integer> resetsRequested;
  public HashMap<Integer, RegistrationRequest> registrationRequests;
  public ArrayList<Integer> adminAuthTokens;
  public ArrayList<Integer> approvedRegistrationIds;

  public ServerState() {
    this.usersDatabase = new DataBase();
    this.authenticatedUsers = new HashMap<>();
    this.resetsRequested = new HashMap<>();
    this.registrationRequests = new HashMap<>();
    this.adminAuthTokens = new ArrayList<>();
    this.approvedRegistrationIds = new ArrayList<>();
    System.out.println("number of users: " + this.usersDatabase.fullData.size());
  }

  /**
   * Ensures all users are still authenticated
   * - This needs to be run while the server is running cycling through the list of authenticated
   *   users, removing any user's with authToken's created later than i.e. 30 min ago.
   */
  public void checkStillAuthenticated() {
    Instant now = Instant.now();
    Instant validLogInTime = now.minus(Duration.ofMinutes(30));
    for (String key : this.authenticatedUsers.keySet()){
      if (this.authenticatedUsers.get(key).getCreatedAt().isBefore(validLogInTime)){
        this.authenticatedUsers.remove(key);
      }
    }
  }

  public void logout(String authToken) {
    this.authenticatedUsers.remove(authToken);
  }

  /**
   * Need to add functionality where password reset requests expire at some point and are removed from
   * resetsRequired list; similar to the checkStillAuthenticated method
   */


}
