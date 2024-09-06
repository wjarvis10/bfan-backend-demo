package com.example.bfandemo.server.requestInfo;

import com.example.bfandemo.userData.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

public class QueryObject {
  @JsonProperty("activeResults")
  public HashMap<Integer, User> activeResults;

  @JsonProperty("query")
  public String query;

  // Default constructor
  public QueryObject() {
  }
}