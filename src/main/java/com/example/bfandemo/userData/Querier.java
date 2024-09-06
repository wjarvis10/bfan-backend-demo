package com.example.bfandemo.userData;

import java.util.ArrayList;
import java.util.HashMap;

public class Querier {

    public DataBase data;

    public HashMap<String, Integer> nameToID;

    public HashMap<String, Integer> titleToID;

    public HashMap<String, Integer> employerToID;


    /**
     * Quieier takes in a data set and produces
     * @param data input database to query
     */
    public Querier(DataBase data){
        this.data = data;
        this.nameToID = new HashMap<String, Integer>();
        this.titleToID = new HashMap<String, Integer>();
        this.employerToID = new HashMap<String, Integer>();

        // populate maps
        for (User user : this.data.fullData.values()){
            this.nameToID.put(user.name.firstName + " " + user.name.lastName, user.userId);
            if (user.type.equals("Alumn")){ // == AccountType.Alumn
                this.titleToID.put(((Alumn) user).currentJob.title, user.userId);
                this.employerToID.put(((Alumn) user).currentJob.company, user.userId);
            }

        }

    }

    // need to edit this so it is returning a Hashmap<Integer, User>
    public HashMap<Integer, User> query(String search){
        HashMap<Integer, User> output = new HashMap<>();
        for (String name : this.nameToID.keySet()) { // loop through full data, will have to do a separate loop for the alumn results (lists diff lens)
            if (search.toLowerCase().contains(name.toLowerCase()) || name.toLowerCase().contains(search.toLowerCase())) {
                output.put(this.data.fullData.get(this.nameToID.get(name)).userId, this.data.fullData.get(this.nameToID.get(name))); // add item by reversing back through query dict
            }
        }

        ArrayList<String> titleList = new ArrayList<String>(this.titleToID.keySet());
        ArrayList<String> employerList = new ArrayList<String>(this.employerToID.keySet());

        for (int i = 0; i < titleList.size(); i++){

            if(search.toLowerCase().contains(titleList.get(i).toLowerCase()) || titleList.get(i).toLowerCase().contains(search.toLowerCase())){ // same logic as before
                output.put(this.data.fullData.get(this.titleToID.get(titleList.get(i))).userId, this.data.fullData.get(this.titleToID.get(titleList.get(i))));
            }
            if(search.toLowerCase().contains(employerList.get(i).toLowerCase()) || employerList.get(i).toLowerCase().contains(search.toLowerCase())){
                output.put(this.data.fullData.get(this.employerToID.get(employerList.get(i))).userId, this.data.fullData.get(this.employerToID.get(employerList.get(i))));
            }
        }
        return output;
    }

}
