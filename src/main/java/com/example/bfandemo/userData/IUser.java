package com.example.bfandemo.userData;

public interface IUser {


    /**
     * Adds another user to this users list of favorites.
     */
    void AddFavorite(int userId);

    /**
     * Removes user from favorites list
     */
    void RemoveFavorite(int userId);

    void EditEmail(String newEmail);
    void EditPhone(String newPhone);
    void EditGradYear(String newGrdYr);
    void EditConcentration(String newConc);
    void EditCertificates(String newCertificates);
    void EditClubs(String editClubs);
    void EditPrimPos(String newPos);
    void EditLinkedIn (String newLinkedIn);

    void EditFirstName (String newFirst);
    void EditLastName (String newLast);

    void EditCountry (String newCountry);
    void EditState (String newState);
    void EditCity (String newCity);

    void EditPassword (String newPassword);

    void EditDescription (String newDescription);


}
