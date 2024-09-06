package com.example.bfandemo.userData;

public class Name {

    public String firstName;

    public String lastName;

    public Name(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Name() {
        // default for deserialization
    }

    public String toString() {
        return (this.firstName + ", " + this.lastName);
    }
}
