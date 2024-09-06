package com.example.bfandemo.userData;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;

@JsonTypeName("Alumn")
public class Alumn extends User{

    public String currentIndustry = "";

    public Job currentJob = new Job("", "");

    public String website = "";

   // public HashMap<String, Job> priorJobs;

    public String gradDegree = "";

    /**
     * Constructor for alumn, assigns variables, uses User class super constructor
     */
    public Alumn(int userId, String password, Name name, String email, String phoneNumber, Location location, String graduationYear,
                 String concentration, String academicCertificates, String clubsOrActivities,
                 String primaryPosition, String linkedInPage, ArrayList<Integer> favorites, String description, String currentIndustry, Job currentJob,
                 String website,String gradDegree){
        super(userId, password, name, email, phoneNumber, location, graduationYear,
                concentration, academicCertificates, clubsOrActivities,
                primaryPosition, linkedInPage, favorites, description);

        this.type = "Alumn"; //AccountType.Alumn;
        this.currentIndustry = currentIndustry;
        this.currentJob = currentJob;
        this.website = website;
        //this.priorJobs = priorJobs;
        this.gradDegree = gradDegree;
    }

    /**
     * Constructor used for the registration process
     * @param userId
     * @param name
     * @param email
     * @param primaryPosition
     * @param graduationYear
     */
    public Alumn(int userId, Name name, String email, String primaryPosition, String graduationYear) {
        super(userId, name, email, primaryPosition, graduationYear);
        this.type = "Alumn";
    }

    /**
     * Default constructor used for deserialization
     */
    public Alumn() {
        // default constructor for deserialization
        this.type = "Alumn";
    }

    public void EditCurrentIndustry(String newIndustry){
        this.currentIndustry = newIndustry;
    }


    public void EditJobTitle(String newTitle){
        this.currentJob.title = newTitle;
    }

    public void EditCompany(String newCompany){
        this.currentJob.company = newCompany;
    }

    public void EditWebsite(String newWebsite){
        this.website = newWebsite;
    }

    public void EditGradDegree(String newGradDegree){
        this.gradDegree = newGradDegree;
    }

//    public void addPriorJob(String jobTitle, String company){
//        Job oldJob = new Job(jobTitle, company);
//        this.priorJobs.put(jobTitle, oldJob);
//    }
//
//    public void removePriorJob(String titleOfJobToRemove){
//        this.priorJobs.remove(titleOfJobToRemove);
//    }
//
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
                "type: " + this.type+ ",\n" +
                "industry: " + this.currentIndustry + ",\n" +
                "job: " + this.currentJob+ ",\n" +
                "website: " + this.website+ ",\n" +
                "gradDegree: " + this.gradDegree + "\n\n");
    }
}
