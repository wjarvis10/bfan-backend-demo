package com.example.bfandemo.userData;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.HashMap;

@JsonTypeName("Player")
public class Player extends User{

//    public String resume;

    public Location expectedLocation = new Location("", "", "");
    public String industryOne = "";
    public String industryTwo = "";
    public String industryThree = "";
    public String goodPriorJob = "";
    public String badPriorJob = "";


    /**
     * Constructor for a player class - user class super constructor used
     * Assigns variables accordinlgy
     */
    public Player(int userId, String password, Name name, String email, String phoneNumber, Location location, String graduationYear,
                  String concentration, String academicCertificates, String clubsOrActivities,
                  String primaryPosition, String linkedInPage, ArrayList<Integer> favorites, String description, Location expectedLocation,
                  String industryOne, String industryTwo, String industryThree, String goodPriorJob, String badPriorJob){

        super(userId, password, name, email, phoneNumber, location, graduationYear,
                concentration, academicCertificates, clubsOrActivities,
                primaryPosition, linkedInPage, favorites, description);

        this.type = "Player" ; // AccountType.Player
        this.expectedLocation = expectedLocation;
        this.industryOne = industryOne;
        this.industryTwo = industryTwo;
        this.industryThree = industryThree;
        this.goodPriorJob = goodPriorJob;
        this.badPriorJob = badPriorJob;
    }

    /**
     * Constructor used for the registration process
     * @param userId
     * @param name
     * @param email
     * @param primaryPosition
     * @param graduationYear
     */
    public Player(int userId, Name name, String email, String primaryPosition, String graduationYear) {
        super(userId, name, email, primaryPosition, graduationYear);
        this.type = "Player";
    }

    /**
     * Default constructor used for deserialization
     */
    public Player() {
        // default constructor for deserialiation
        this.type = "Player" ;
    }

    public void editExpectedCountry(String newExpectedCountry){
        this.expectedLocation.country = newExpectedCountry;
    }
    public void editExpectedState(String newExpectedState){
        this.expectedLocation.state = newExpectedState;
    }
    public void editExpectedCity(String newExpectedCity){
        this.expectedLocation.city = newExpectedCity;
    }

    public void editIndustryOne(String newIndustry){
        this.industryOne = newIndustry;
    }
    public void editIndustryTwo(String newIndustry){
        this.industryTwo = newIndustry;
    }
    public void editIndustryThree(String newIndustry){
        this.industryThree = newIndustry;
    }

    public void editGoodPriorJob(String newGoodPriorJob) {
        this.goodPriorJob = newGoodPriorJob;
    }

    public void editBadPriorJob(String badPriorJob) {
        this.badPriorJob = badPriorJob;
    }

    /**
     * Takes given players data and returns and alumn profile - to reduce heap garbage,
     * will remove delete player profile after copying over ID in the databse function
     * @return their alumn data
     */
    public Alumn Graduate(String currentIndustry, Job currentJob, String website, HashMap<String, Job> priorJobs, String gradDegree){
        Alumn newAlumn = new Alumn(this.userId, this.password, this.name, this.email, this.phoneNumber, this.location, this.graduationYear,
                this.concentration, this.academicCertificates, this.clubsOrActivities, this.primaryPosition,
                this.linkedInPage, this.favorites, this.description, currentIndustry, currentJob, website, gradDegree);

        newAlumn.favorites = this.favorites;

        return newAlumn;
    }

    public String toString() {
        return (
            "userId: " + this.userId + ",\n" +
                "password: " + this.password + ",\n" +
                "name: " + this.name + ",\n" +
                "email: " + this.email + ",\n" +
                "phone: " + this.phoneNumber + ",\n" +
                "location: " + this.location + ",\n" +
                "gradYear: " + this.graduationYear + ",\n" +
                "concentration: " + this.concentration + ",\n" +
                "academic certificates: " + this.academicCertificates + ",\n" +
                "clubs: " + this.clubsOrActivities + ",\n" +
                "position: " + this.primaryPosition + ",\n" +
                "linkedIn: " + this.linkedInPage + ",\n" +
                "favs: " + this.favorites + ",\n" +
                "description: " + this.description + ",\n" +
                "type: " + this.type + ",\n" +
                "location: " + this.expectedLocation + ",\n" +
                "industry1: " + this.industryOne + ",\n" +
                "industry2: " + this.industryTwo + ",\n" +
                "industry3: " + this.industryThree + ",\n" +
                "good work: " + this.goodPriorJob + ",\n" +
                "bad work: " + this.badPriorJob + "\n\n"
            );
    }

}
