package com.example.bfandemo.dataHandlers;

import com.example.bfandemo.userData.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SQLHandler {

//    private static final String JDBC_URL = "jdbc:mysql://your_database_url:your_port/your_database_name";
//    private static final String USERNAME = "your_username";
//    private static final String PASSWORD = "your_password";
    private static final String JDBC_URL = "jdbc:mysql://bfan-database-1.c7yqkiekae2y.us-east-1.rds.amazonaws.com:3306/bfanUserData";
    private static final String USERNAME = "bfanAdmin";
    private static final String PASSWORD = "BrownFootball1!";

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    /**
     * Parses SQL data and produces a hashmap for Database.
     * @return HashMap with id as key and user class as value
     */
    public static HashMap<Integer, User> fetchUserData() {
        HashMap<Integer, User> userMap = new HashMap<>();

        try (Connection connection = createConnection()) {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    int userId = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    String strType = resultSet.getString("account_type");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Name name = new Name(firstName, lastName);
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    String country = resultSet.getString("country");
                    String state = resultSet.getString("state");
                    String city = resultSet.getString("city");
                    Location location = new Location(country, state, city);
                    String graduationYear = resultSet.getString("grad_year");
                    String concentration = resultSet.getString("concentration");
                    String academicCertificates = resultSet.getString("academic_certificates");
                    String clubsOrActivities = resultSet.getString("clubs_activities");
                    String primaryPosition = resultSet.getString("primary_position");
                    String linkedInPage = resultSet.getString("linkedin");
                    String description = resultSet.getString("description");
                    String favIDs = resultSet.getString("favorites"); // will format as string "id,id,id,id"
                    ArrayList<String> strFavs = new ArrayList<>(Arrays.asList(favIDs.split(",")));
                    ArrayList<Integer> favorites = new ArrayList<>();
                        for (String str : strFavs) {
                            if (!str.equals("")) {
                                favorites.add(Integer.parseInt(str));
                            }
                        }


//                        try {
//                            favorites.add(Integer.parseInt(str));
//                        } catch (java.lang.NumberFormatException e) {
//                            System.out.println(e);
//                        }


                    if (strType.equals("player")){
                        String type = "Player";
                        String expectedCountry = resultSet.getString("expected_country");
                        String expectedState = resultSet.getString("expected_state");
                        String expectedCity = resultSet.getString("expected_city");
                        String industryOne = resultSet.getString("industry_one");
                        String industryTwo = resultSet.getString("industry_two");
                        String industryThree = resultSet.getString("industry_three");
                        String goodPriorJob = resultSet.getString("good_prior_work");
                        String badPriorJob = resultSet.getString("bad_prior_work");
                        Location expectedLocation = new Location(expectedCountry, expectedState, expectedCity);
                        Player player = new Player(userId, password, name, email, phoneNumber, location, graduationYear,
                                concentration, academicCertificates, clubsOrActivities,
                                primaryPosition, linkedInPage, favorites, description, expectedLocation,
                                industryOne, industryTwo, industryThree, goodPriorJob, badPriorJob);
                        userMap.put(userId, player);
                    }
                    else {
                        String type = "Alumn";
                        String currentIndustry = resultSet.getString("current_industry");
                        String jobtitle = resultSet.getString("job_title");
                        String company = resultSet.getString("company");
                        Job currentJob = new Job(jobtitle, company);
                        String website = resultSet.getString("website");
//                        String bundledPriorJobs = resultSet.getString("prior_jobs"); // will format as string - "title:company,title:company"
//                        String[] splitJobs = bundledPriorJobs.split(",");
//                        HashMap<String, Job> priorJobs = new HashMap<String, Job>();
//                        for (String str : splitJobs){
//                            String[] split = str.split(":");
//                            String title = split[0];
//                            String co = split[1];
//                            Job newJob = new Job(title, co);
//                            priorJobs.put(title, newJob);
//                        }
                        String gradDegree = resultSet.getString("graduate_degree");

                        Alumn alumn = new Alumn(userId, password, name, email, phoneNumber, location, graduationYear,
                                concentration, academicCertificates, clubsOrActivities,
                                primaryPosition, linkedInPage, favorites, description, currentIndustry, currentJob,
                                website, gradDegree);
                        userMap.put(userId, alumn);
                    }


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userMap;
    }
    /**
     * Updates a specific column for a given user ID in the SQL table.
     * @param userId User ID to identify the user to be updated.
     * @param columnName Name of the column to be updated.
     * @param newValue New value to be set for the specified column.
     */
    public static void updateUser(int userId, String columnName, String newValue) {
        try (Connection connection = createConnection()) {
            String updateQuery = "UPDATE users SET " + columnName + " = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, userId);

                int rowsAffected = preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds another ID to the "favorites" column for a given user ID in the users table.
     * @param userId User ID to identify the user.
     * @param newFavoriteId ID to be added to the "favorites" column.
     */
    public static void addFavorite(int userId, int newFavoriteId) {
        try (Connection connection = createConnection()) {
            // Fetch the existing favorites string for the user
            String existingFavorites = fetchExistingFavorites(connection, userId);

                // Concatenate the new favorite ID to the existing string
            String newFavorites = existingFavorites + "," + newFavoriteId;

                // Update the "favorites" column in the database
            updateUser(userId, "favorites", newFavorites);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the existing "favorites" string for a given user ID.
     * @param connection Database connection.
     * @param userId User ID to identify the user.
     * @return Existing "favorites" string.
     * @throws SQLException If a database access error occurs.
     */
    private static String fetchExistingFavorites(Connection connection, int userId) throws SQLException {
        String query = "SELECT favorites FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("favorites");
                }
            }
        }
        // Return an empty string if the user ID is not found or an error occurs
        return "";
    }

    /**
     * Removes a favorite ID from the "favorites" column for a given user ID in the users table.
     * @param userId User ID to identify the user.
     * @param favoriteId ID to be removed from the "favorites" column.
     */
    public static void removeFavorite(int userId, int favoriteId) {
        try (Connection connection = createConnection()) {
            // Fetch the existing favorites string for the user
            String existingFavorites = fetchExistingFavorites(connection, userId);

            // Remove the specified favorite ID from the existing string
            String newFavorites = removeFavoriteId(existingFavorites, favoriteId);

            // Update the "favorites" column in the database
            updateUser(userId, "favorites", newFavorites);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a specific ID from the "favorites" string.
     * @param favoritesString Existing "favorites" string.
     * @param favoriteId ID to be removed.
     * @return Updated "favorites" string after removal.
     */
    private static String removeFavoriteId(String favoritesString, int favoriteId) {
        // Split the existing string into an array of favorite IDs
        String[] favoriteIds = favoritesString.split(",");

        // Create a StringBuilder to construct the updated favorites string
        StringBuilder updatedFavorites = new StringBuilder();

        // Iterate through the array of favorite IDs
        for (String id : favoriteIds) {
            if (!id.equals("")) {
                // Parse the ID as an integer
                int currentId = Integer.parseInt(id);
                // Check if the ID is not the one to be removed
                if (currentId != favoriteId) {
                    // Append the ID to the updated favorites string
                    updatedFavorites.append(id).append(",");
                }
            }
        }

        // Remove the trailing comma and return the updated favorites string
        return updatedFavorites.length() > 0 ? updatedFavorites.substring(0, updatedFavorites.length() - 1) : "";
    }

    public static void addNewUser(User user) {
        try (Connection connection = createConnection()) {
            String insertQuery = "INSERT INTO users (id, password, account_type, first_name, last_name, email, phone_number, country, " +
                    "state, city, grad_year, concentration, academic_certificates, clubs_activities, " +
                    "primary_position, linkedin, favorites, description, expected_country, expected_state, expected_city, " +
                    "industry_one, industry_two, industry_three, good_prior_work, bad_prior_work, current_industry, job_title, company, " +
                    "website, graduate_degree) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            //31 fields

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set parameters based on the User object
                preparedStatement.setInt(1, user.userId);
                preparedStatement.setString(2, user.password);
                preparedStatement.setString(4, user.name.firstName);
                preparedStatement.setString(5, user.name.lastName);
                preparedStatement.setString(6, user.email);
                preparedStatement.setString(7, user.phoneNumber);
                preparedStatement.setString(8, user.location.country);
                preparedStatement.setString(9, user.location.state);
                preparedStatement.setString(10, user.location.city);
                preparedStatement.setString(11, user.graduationYear);
                preparedStatement.setString(12, user.concentration);
                preparedStatement.setString(13, user.academicCertificates);
                preparedStatement.setString(14, user.clubsOrActivities);
                preparedStatement.setString(15, user.primaryPosition);
                preparedStatement.setString(16, user.linkedInPage);
                preparedStatement.setString(17, packageFavs(user.favorites));
                preparedStatement.setString(18, user.description);



                if (user.type.equals("Player")) {
                    // Set additional parameters for Player
                    preparedStatement.setString(3, "player");
                    preparedStatement.setString(19, ((Player) user).expectedLocation.country);
                    preparedStatement.setString(20, ((Player) user).expectedLocation.state);
                    preparedStatement.setString(21, ((Player) user).expectedLocation.city);
                    preparedStatement.setString(22, ((Player) user).industryOne);
                    preparedStatement.setString(23, ((Player) user).industryTwo);
                    preparedStatement.setString(24, ((Player) user).industryThree);
                    preparedStatement.setString(25, ((Player) user).goodPriorJob);
                    preparedStatement.setString(26, ((Player) user).badPriorJob);
                    preparedStatement.setString(27, "N/A");
                    preparedStatement.setString(28, "N/A");
                    preparedStatement.setString(29, "N/A");
                    preparedStatement.setString(30, "N/A");
                    preparedStatement.setString(31, "N/A");

                } else if (user.type.equals("Alumn")) {
                    // Set additional parameters for Alumn
                    preparedStatement.setString(3, "alumn");
                    preparedStatement.setString(19, "N/A");
                    preparedStatement.setString(20, "N/A");
                    preparedStatement.setString(21, "N/A");
                    preparedStatement.setString(22, "N/A");
                    preparedStatement.setString(23, "N/A");
                    preparedStatement.setString(24, "N/A");
                    preparedStatement.setString(25, "N/A");
                    preparedStatement.setString(26, "N/A");
                    preparedStatement.setString(27, ((Alumn) user).currentIndustry);
                    preparedStatement.setString(28, ((Alumn) user).currentJob.title);
                    preparedStatement.setString(29, ((Alumn) user).currentJob.company);
                    preparedStatement.setString(30, ((Alumn) user).website);
                    preparedStatement.setString(31, ((Alumn) user).gradDegree);
                }

                // Execute the insert query
                int rowsAffected = preparedStatement.executeUpdate();

//                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper to package favorites into SQL format
     * @param favorites favorites list of ids
     * @return
     */
    public static String packageFavs(ArrayList<Integer> favorites) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < favorites.size(); i++) {
            result.append(favorites.get(i));

            // Append a comma if it's not the last element
            if (i < favorites.size() - 1) {
                result.append(",");
            }
        }

        return result.toString();
    }

    /**
     * Deletes a user from the users table based on the user's ID.
     * @param userId User ID to identify the user to be deleted.
     */
    public static void deleteUser(int userId) {
        try (Connection connection = createConnection()) {
            String deleteQuery = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, userId);

                int rowsAffected = preparedStatement.executeUpdate();

//                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}









