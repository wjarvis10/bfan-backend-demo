package com.example.bfandemo.userData;

import com.example.bfandemo.dataHandlers.SQLHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class DataBase {

    public final HashMap<Integer, User> fullData;

    public HashMap<String, ArrayList<String>> countryStateMap;

    public ArrayList<String> usedIndustries;

    public ArrayList<Integer> usedClassYears;

    // need to handle sending database to frontend without user's passwords

    // constructor will need to take in SQL data and produce the HashMap fulldata

    /**
     * Initial constructor which pulls SQL data
     */
    public DataBase(){
        this.fullData = SQLHandler.fetchUserData();

        this.countryStateMap = new HashMap<>();
        HashSet<String> usedIndustries = new HashSet<>();
        HashSet<Integer> usedClassYears = new HashSet<>();

        for (User user : this.fullData.values()){
            if (this.countryStateMap.containsKey(user.location.country)){
                if (!this.countryStateMap.get(user.location.country).contains(user.location.state)) {
                    this.countryStateMap.get(user.location.country).add(user.location.state);
                    Collections.sort(this.countryStateMap.get(user.location.country));
                }
            } else {
                ArrayList<String> states = new ArrayList<>();
                states.add(user.location.state);
                this.countryStateMap.put(user.location.country, states);
            }
            if (user.type.equals("Alumn")) {
                if (((Alumn) user).currentIndustry != null) {
                    usedIndustries.add(((Alumn) user).currentIndustry);
                }
            }
            if (user.graduationYear != null) {
                usedClassYears.add(Integer.parseInt(user.graduationYear));
            }
        }

        ArrayList<String> industriesList = new ArrayList<>(usedIndustries);
        Collections.sort(industriesList);

        ArrayList<Integer> classYearsList = new ArrayList<>(usedClassYears);
        Collections.sort(classYearsList, Collections.reverseOrder());

        this.usedIndustries = industriesList;
        this.usedClassYears = classYearsList;

    }

    /**
     * Constructor for filter method
     * @param fullData completed filtered data
     */
    public DataBase(HashMap<Integer, User> fullData){
        this.fullData = fullData;
    }

    public DataBase filterForAlumn() {
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.type.equals("Alumn")){ // == AccountType.Alumn
                dataMap.put(user.userId, user);
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForPlayer() {
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.type.equals("Player") ){ // == AccountType.Player
                dataMap.put(user.userId, user);
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForIndustry(String industry) {
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.type.equals("Alumn") ){ // == AccountType.Alumn
                if (((Alumn) user).currentIndustry.equalsIgnoreCase(industry)) {
                    dataMap.put(user.userId, user);
                }
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForCountry(String country){
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.location.country.equalsIgnoreCase(country)){
                dataMap.put(user.userId, user);
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForState(String state){
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.location.state.equalsIgnoreCase(state)){
                dataMap.put(user.userId, user);
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForCity(String city){
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (User user : this.fullData.values()){
            if (user.location.city.equalsIgnoreCase(city)){
                dataMap.put(user.userId, user);
            }
        }
        return new DataBase(dataMap);
    }

    public DataBase filterForYear(String year, String period){
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        if (period.equals("On or before")) {
            for (User user : this.fullData.values()) {
                if (Integer.parseInt(user.graduationYear) <= Integer.parseInt(year)) {
                    dataMap.put(user.userId, user);
                }
            }
        } else if (period.equals("Exactly")) {
            for (User user : this.fullData.values()) {
                if (Integer.parseInt(user.graduationYear) == Integer.parseInt(year)) {
                    dataMap.put(user.userId, user);
                }
            }
        } else if (period.equals("After")) {
            for (User user : this.fullData.values()) {
                if (Integer.parseInt(user.graduationYear) > Integer.parseInt(year)) {
                    dataMap.put(user.userId, user);
                }
            }
        } else {
            System.out.println("Error: Somehow the period was neither On or before, Exactly, or After");
        }
        return new DataBase(dataMap);
    }

    /**
     * Returns a filtered data base for only a users network
     * @param favorites list of user Ids
     * @return database
     */
    public DataBase filterForFavs(ArrayList<Integer> favorites){
        HashMap<Integer, User> dataMap = new HashMap<Integer, User>();
        for (int id : favorites){
            User fav = this.fullData.get(id);
            if (fav != null) {
                dataMap.put(fav.userId, fav);
            } else {
                System.out.println("Could not find corresponding user to userId: " + id);
            }
        }
        return new DataBase(dataMap);
    }

    /**
     * Remove player from the data and point the user to null - Need to also have this remove them from SQL
     * data
     * @param ID userID
     * @param user user object
     */
    public void deleteAcct(int ID, User user){
        this.fullData.remove(ID);
        user = null;
        SQLHandler.deleteUser(ID);
    }


    public void graduatePlayerInData(int ID, Player player,  String currentIndustry, Job currentJob, String website, HashMap<String, Job> priorJobs, String gradDegree){
        Alumn newAlumn = player.Graduate(currentIndustry, currentJob, website, priorJobs, gradDegree);
        this.deleteAcct(ID, player);
        this.createAlumnAccount(newAlumn.password, newAlumn.name, newAlumn.email, newAlumn.phoneNumber, newAlumn.location, newAlumn.graduationYear,
                newAlumn.concentration, newAlumn.academicCertificates, newAlumn.clubsOrActivities,
                newAlumn.primaryPosition, newAlumn.linkedInPage, newAlumn.description, newAlumn.currentIndustry, newAlumn.currentJob,
                newAlumn.website, newAlumn.gradDegree); //  need to move favorites over somehow.
    }

    /**
     * Edits a user given a field to edit and the desired change. Also makes this change in SQL data.
     * Fields should be input as the SQL column names.
     * id
     * account_type
     * first_name
     * last_name
     * email
     * phone_number
     * country
     * state
     * city
     * grad_year
     * concentration
     * academic_certificates
     * clubs_activities
     * primary_position
     * linkedin
     * favorites - in format (id,id,id,id) as one string of ids separated by commas
     * gpa
     * current_industry
     * job_title
     * company
     * prior_jobs - in format (title:company,title:company)
     * graduate_degree
     */
    public void editUser(String field, String change, int userId){
        if (field.equals("first_name")){
            this.fullData.get(userId).EditFirstName(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("password")){
            this.fullData.get(userId).EditPassword(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("last_name")) {
            this.fullData.get(userId).EditLastName(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("email")) {
            this.fullData.get(userId).EditEmail(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("phone_number")) {
            this.fullData.get(userId).EditPhone(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("country")) {
            this.fullData.get(userId).EditCountry(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("state")) {
            this.fullData.get(userId).EditState(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("city")) {
            this.fullData.get(userId).EditCity(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("grad_year")) {
            this.fullData.get(userId).EditGradYear(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("concentration")) {
            this.fullData.get(userId).EditConcentration(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("academic_certificates")) {
            this.fullData.get(userId).EditCertificates(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("clubs_activities")) {
            this.fullData.get(userId).EditClubs(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("primary_position")) {
            this.fullData.get(userId).EditPrimPos(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("linkedin")) {
            this.fullData.get(userId).EditLinkedIn(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("description")) {
            this.fullData.get(userId).EditDescription(change);
            SQLHandler.updateUser(userId, field, change);


            // player fields
        } else if (field.equals("expected_country")) {
            ((Player) this.fullData.get(userId)).editExpectedCountry(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("expected_state")) {
            ((Player) this.fullData.get(userId)).editExpectedState(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("expected_city")) {
            ((Player) this.fullData.get(userId)).editExpectedCity(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("industry_one")) {
            ((Player) this.fullData.get(userId)).editIndustryOne(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("industry_two")) {
            ((Player) this.fullData.get(userId)).editIndustryTwo(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("industry_three")) {
            ((Player) this.fullData.get(userId)).editIndustryThree(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("bad_prior_work")) {
            ((Player) this.fullData.get(userId)).editBadPriorJob(change);
            SQLHandler.updateUser(userId, field, change);
        } else if (field.equals("good_prior_work")) {
            ((Player) this.fullData.get(userId)).editGoodPriorJob(change);
            SQLHandler.updateUser(userId, field, change);



            // alumn fields
        } else if (field.equals("current_industry")) {
            ((Alumn) this.fullData.get(userId)).EditCurrentIndustry(change);
            SQLHandler.updateUser(userId, field, change);
        }
        else if (field.equals("job_title")) {
            ((Alumn) this.fullData.get(userId)).EditJobTitle(change);
            SQLHandler.updateUser(userId, field, change);
        }
        else if (field.equals("company")) {
            ((Alumn) this.fullData.get(userId)).EditCompany(change);
            SQLHandler.updateUser(userId, field, change);
        }
        else if (field.equals("website")) {
            ((Alumn) this.fullData.get(userId)).EditWebsite(change);
            SQLHandler.updateUser(userId, field, change);
        }
        else if (field.equals("graduate_degree")) {
            ((Alumn) this.fullData.get(userId)).EditGradDegree(change);
            SQLHandler.updateUser(userId, field, change);
        }

    }

    /**
     * Adds a users new favorite within SQL and the app data
     * @param userId ID of user
     * @param favId ID of user to add to favs
     */
    public void addUserFavorite(int userId, int favId){
        this.fullData.get(userId).AddFavorite(favId);
        SQLHandler.addFavorite(userId, favId);
    }

    /**
     * Removes a users new favorite within SQL and the app data
     * @param userId ID of user
     * @param favId ID of user to add to favs
     */
    public void removeUserFavorite(int userId, int favId){
        this.fullData.get(userId).RemoveFavorite(favId);
        SQLHandler.removeFavorite(userId, favId);
    }

    /**
     * Creates a new User ID
     */
    public Integer generateId(){
        Random random = new Random();
        int minID = 100000;  // Minimum six-digit number
        int maxID = 999999;  // Maximum six-digit number
        int randomID;

        // Generate a random six-digit ID until one is found that is not in the existing set
        do {
            randomID = random.nextInt((maxID - minID) + 1) + minID;
        } while (this.fullData.keySet().contains(randomID));

        return randomID;
    }

    /**
     * Creates a new player in the dataBase
     */
    public void createPlayerAccount(String password, Name name, String email, String phoneNumber, Location location, String graduationYear,
                                    String concentration, String academicCertificates, String clubsOrActivities,
                                    String primaryPosition, String linkedInPage, String description, Location expectedLocation,
                                    String industryOne, String industryTwo, String industryThree, String badPriorJob, String goodPriorJob){

        int userId = this.generateId();
        String hashedPassword = PasswordHashing.hashPassword(password);
        Player newPlayer = new Player(userId, hashedPassword, name, email, phoneNumber, location, graduationYear,
                concentration, academicCertificates, clubsOrActivities,
                primaryPosition, linkedInPage, new ArrayList<Integer>(), description, expectedLocation,
                industryOne, industryTwo, industryThree, goodPriorJob, badPriorJob);
        this.fullData.put(newPlayer.userId, newPlayer);

        SQLHandler.addNewUser(newPlayer);

    }

    /**
     * Adding player account to the database post registration request approval
     * @param player
     */
    public void addPlayerAccount(Player player) {
        this.fullData.put(player.userId, player);
        SQLHandler.addNewUser(player);
    }

    /**
     * Creates a new alumn in the database
     */
    public void createAlumnAccount(String password, Name name, String email, String phoneNumber, Location location, String graduationYear,
                                   String concentration, String academicCertificates, String clubsOrActivities,
                                   String primaryPosition, String linkedInPage, String description, String currentIndustry, Job currentJob,
                                   String website,String gradDegree){

        int userId = this.generateId();
        String hashedPassword = PasswordHashing.hashPassword(password);
        Alumn newAlumn = new Alumn(userId, hashedPassword, name, email, phoneNumber, location, graduationYear,
                concentration, academicCertificates, clubsOrActivities,
                primaryPosition, linkedInPage, new ArrayList<Integer>(), description, currentIndustry, currentJob, website, gradDegree);

        this.fullData.put(newAlumn.userId, newAlumn);

        SQLHandler.addNewUser(newAlumn);

    }

    /**
     * Adding alumn account to the database post registration request approval
     * @param alumn
     */
    public void addAlumnAccount(Alumn alumn) {
        this.fullData.put(alumn.userId, alumn);
        SQLHandler.addNewUser(alumn);
    }

    /**
     * Updates the filter lists when a new user is added to the database
     * @param userId
     */
    public void updateFilterLists(Integer userId) {
        User user = this.fullData.get(userId);
        // update the country-state map
        if (this.countryStateMap.containsKey(user.location.country)){
            this.countryStateMap.get(user.location.country).add(user.location.state);
            Collections.sort(this.countryStateMap.get(user.location.country));
        } else {
            ArrayList<String> states = new ArrayList<>();
            states.add(user.location.state);
            this.countryStateMap.put(user.location.country, states);
        }
        // update the industries list
        if (user.type.equals("Alumn")) {
            if (((Alumn) user).currentIndustry != null) {
                if (!this.usedIndustries.contains(((Alumn) user).currentIndustry)) {
                    this.usedIndustries.add(((Alumn) user).currentIndustry);
                    Collections.sort(this.usedIndustries);
                }
            }
        }
        // update the class years list
        if (user.graduationYear != null) {
            Integer year = Integer.parseInt(user.graduationYear);
            if (!this.usedClassYears.contains(year)) {
                this.usedClassYears.add(year);
                Collections.sort(this.usedClassYears, Collections.reverseOrder());
            }
        }
    }

}













