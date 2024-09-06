package com.example.bfandemo.userData;

public class Job {
    public String title;

    public String company;

    public Job(String title, String company){
        this.title = title;
        this.company = company;
    }

    public Job() {
        // default for deserialization
    }

    public String toString() {
        return (this.title + " at " + this.company);
    }
}
