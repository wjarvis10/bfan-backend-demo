package com.example.bfandemo.userData;

public class Location {

    public String country;

    public String state;

    public String city;

    public Location(String country, String state, String city){
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public Location() {
        // default for deserialization
    }
}
