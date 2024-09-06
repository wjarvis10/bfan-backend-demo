package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;
import java.util.HashMap;

public class FavsResponse {

  public boolean success;
  public HashMap<Integer, User> results;

  public FavsResponse(boolean success, HashMap<Integer, User> favUsers) {
    this.success = success;
    this.results = favUsers;
  }

}
