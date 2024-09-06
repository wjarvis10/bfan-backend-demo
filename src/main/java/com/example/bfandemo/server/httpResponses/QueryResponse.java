package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;
import java.util.HashMap;

public class QueryResponse {


    public boolean success;
    public HashMap<Integer, User> results;


    public QueryResponse(boolean success, HashMap<Integer, User> results){
        this.success = success;
        this.results = results;
    }

}
