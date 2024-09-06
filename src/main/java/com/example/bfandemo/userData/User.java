package com.example.bfandemo.userData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Alumn.class, name = "Alumn"),
    @JsonSubTypes.Type(value = Player.class, name = "Player")
})

public abstract class User implements IUser {

  public int userId;

  public String password = "";

  //public AccountType type;
  public String type = "";

  public Name name = new Name("", "");
  public String email = "";
  public String phoneNumber = "0"; // maybe diff data type
  public Location location = new Location("", "", "");
  public String graduationYear = ""; // maybe diff data type
  public String concentration = "";
  public String academicCertificates = "";
  public String clubsOrActivities = "";
  public String primaryPosition = "";
  public String linkedInPage = "";

  public ArrayList<Integer> favorites = new ArrayList<>();

  public String description = "";

  public User(int UserId, String password, Name name, String email, String phoneNumber, Location location, String graduationYear,
              String concentration, String academicCertificates, String clubsOrActivities,
              String primaryPosition, String linkedInPage, ArrayList<Integer> favorites, String description){

    this.userId = UserId; // need to come out with ID creation method
    this.password = password;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.location = location;
    this.graduationYear = graduationYear;
    this.concentration = concentration;
    this.academicCertificates = academicCertificates;
    this.clubsOrActivities = clubsOrActivities;
    this.primaryPosition = primaryPosition;
    this.linkedInPage = linkedInPage;
    this.favorites = favorites;
    this.description = description;

  }

  /**
   * Constructor for registration process
   * @param userId
   * @param name
   * @param email
   * @param primaryPosition
   * @param graduationYear
   */
  public User(int userId, Name name, String email, String primaryPosition, String graduationYear) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.primaryPosition = primaryPosition;
    this.graduationYear = graduationYear;
  }

  /**
   * Default Constructor for deserializing
   */
  public User() {
    // default constructor for deserializing
  }

  @Override
  public void EditCertificates(String newCertificates) {
    this.academicCertificates = newCertificates;
  }

  @Override
  public void EditClubs(String editClubs) {
    this.clubsOrActivities = editClubs;
  }

  @Override
  public void EditConcentration(String newConc) {
    this.concentration = newConc;
  }

  @Override
  public void EditEmail(String newEmail) {
    this.email = newEmail;
  }

  @Override
  public void EditGradYear(String newGrdYr) {
    this.graduationYear = newGrdYr;
  }

  @Override
  public void EditLinkedIn(String newLinkedIn) {
    this.linkedInPage = newLinkedIn;
  }

  @Override
  public void EditPhone(String newPhone) {
    this.phoneNumber = newPhone;
  }

  @Override
  public void EditPrimPos(String newPos) {
    this.primaryPosition = newPos;
  }

  @Override
  public void AddFavorite(int userId) {
    this.favorites.add(userId);
  }

  @Override
  public void RemoveFavorite(int userId) {
    System.out.println(Integer.valueOf(Integer.toString(userId)));
    this.favorites.remove((Integer)userId);
  }

  @Override
  public void EditLastName(String newLast) {
    this.name.lastName = newLast;
  }

  @Override
  public void EditFirstName(String newFirst) {
    this.name.firstName = newFirst;
  }

  @Override
  public void EditCountry(String newCountry) {
    this.location.country = newCountry;
  }

  @Override
  public void EditCity(String newCity) {
    this.location.city = newCity;
  }

  @Override
  public void EditState(String newState) {
    this.location.state = newState;
  }

  @Override
  public void EditPassword(String newPassword) {
    this.password = newPassword;
  }

  @Override
  public void EditDescription(String newDescription) {
    this.description = newDescription;
  }
}


