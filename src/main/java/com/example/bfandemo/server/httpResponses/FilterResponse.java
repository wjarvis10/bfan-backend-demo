package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.User;
import java.util.HashMap;

public class FilterResponse {

    public boolean success;
    public HashMap<Integer, User> results;

    public FilterResponse(boolean success, HashMap<Integer, User> activeResults){
        this.success = success;
        this.results = activeResults;
    }

}
