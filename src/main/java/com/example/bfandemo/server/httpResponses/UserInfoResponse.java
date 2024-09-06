package com.example.bfandemo.server.httpResponses;

import com.example.bfandemo.userData.DataBase;
import com.example.bfandemo.userData.User;
import java.util.ArrayList;

public class UserInfoResponse {

    public User user;
    public DataBase dataBase;

    public ArrayList<Integer> activeResults;

    public UserInfoResponse(User user, DataBase dataBase){
        this.user = user;
        this.dataBase = dataBase;
        this.activeResults = new ArrayList<Integer>(dataBase.fullData.keySet());

    }



}
