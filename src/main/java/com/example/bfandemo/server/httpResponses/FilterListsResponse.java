package com.example.bfandemo.server.httpResponses;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterListsResponse {
  public boolean success;
  public HashMap<String, ArrayList<String>> locations;
  public ArrayList<String> industries;
  public ArrayList<Integer> classYears;

  public FilterListsResponse(boolean success, HashMap<String, ArrayList<String>> locations, ArrayList<String> industries, ArrayList<Integer> classYears) {
    this.success = success;
    this.locations = locations;
    this.industries = industries;
    this.classYears = classYears;
  }
}
