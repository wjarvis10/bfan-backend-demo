package com.example.bfandemo.server.controllers;

import com.example.bfandemo.authenticationData.AuthenticatedUser;
import com.example.bfandemo.server.ServerState;
import com.example.bfandemo.server.httpResponses.*;
import com.example.bfandemo.server.requestInfo.*;
import com.example.bfandemo.server.services.QueryDeserializer;
import com.example.bfandemo.userData.AuthErrorException;
import com.example.bfandemo.userData.DataBase;
import com.example.bfandemo.userData.Querier;
import com.example.bfandemo.userData.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api")
@CrossOrigin(origins = "http://bfanetwork.com")
public class ApiController {

  @Autowired
  private ServerState serverState;


  private User authenticate(String authToken) throws AuthErrorException {

    AuthenticatedUser validUser = this.serverState.authenticatedUsers.get(authToken);

    if (validUser == null) {
      throw new AuthErrorException("User not Authenticated");
    } else {
      validUser.setCreatedAt(Instant.now());
      this.serverState.authenticatedUsers.replace(authToken, validUser);
      // is this creating heap junk?
      return validUser.getUser();
    }

  }

  // api endpoints

  @GetMapping("/name")  // Maps GET requests to /name
  public ResponseEntity name(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      User currentUser = authenticate(authToken);
      return ResponseEntity.ok(new GetResponse(true, currentUser.name));
    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @GetMapping("/verifyAuth")  // Maps GET requests to /name
  public ResponseEntity verifyAuth(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      User currentUser = authenticate(authToken);
      return ResponseEntity.ok(new VerificationResponse(true));
    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @PostMapping("/editUser")
  public ResponseEntity editUser(@RequestHeader(value = "Authorization", required = true) String authToken,
                                 @RequestBody HashMap<String, String> editsMap) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is currently accessing the editUser endpoint.");
      // System.out.println(editsMap);

      for (String field : editsMap.keySet()){
        serverState.usersDatabase.editUser(field, editsMap.get(field), currentUser.userId);
      }
      return ResponseEntity.ok(new PostResponse(true));

    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @PostMapping("/editFavs")
  public ResponseEntity editFavs(@RequestHeader(value = "Authorization", required = true) String authToken,
                                 @RequestBody EditFavsInfo editFavsInfo) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the editFavs endpoint.");
      System.out.println("action: " + editFavsInfo.action + ", favId: " + editFavsInfo.favId);
      int userId = Integer.parseInt(editFavsInfo.favId);
      if (editFavsInfo.action.equals("add")) {
        // add the user to the current user's favorites
        serverState.usersDatabase.addUserFavorite(currentUser.userId, userId);
        // pull the updated list of favorites
        DataBase dataBase = new DataBase(serverState.usersDatabase.fullData);
        ArrayList<Integer> favs = currentUser.favorites;
        DataBase favUsers = dataBase.filterForFavs(favs);
        return ResponseEntity.ok(new FavsResponse(true, favUsers.fullData));
      } else if (editFavsInfo.action.equals("remove")) {
        if (currentUser.favorites.contains(userId)) {
          // remove the user from the current user's favorites
          serverState.usersDatabase.removeUserFavorite(currentUser.userId, userId);
          // pull the updated list of favorites
          DataBase dataBase = new DataBase(serverState.usersDatabase.fullData);
          ArrayList<Integer> favs = currentUser.favorites;
          DataBase favUsers = dataBase.filterForFavs(favs);
          return ResponseEntity.ok(new FavsResponse(true, favUsers.fullData));
        } else {
          System.out.println("Error: The userId: " + userId + " was not in the current user's fav list");
        }
      }

      return ResponseEntity.ok(new PostResponse(false));

    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @GetMapping("/userInfo")
  public ResponseEntity userInfo(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      User currentUser = authenticate(authToken);
      return ResponseEntity.ok(new UserInfoResponse(currentUser, serverState.usersDatabase));
    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @PostMapping("/filter")
  public ResponseEntity filter(@RequestHeader(value = "Authorization", required = true) String authToken,
      @RequestBody Map<String, FilterObject> filterInfo) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the filter endpoint");

      DataBase filteredData = new DataBase(serverState.usersDatabase.fullData);

      Boolean yearFilterApplied = false;
      String year = "";
      String period = "";

      for (Map.Entry<String, FilterObject> entry : filterInfo.entrySet()) {
        String filter = entry.getKey();
        FilterObject filterData = entry.getValue();
        Boolean applied = filterData.applied;
        String value = filterData.value;
        //System.out.println(filter + " - applied: " + applied + ", value: " + value);

        if (filter.equals("alumn") & applied) {
          filteredData = filteredData.filterForAlumn();
        } else if (filter.equals("player") & applied) {
          filteredData = filteredData.filterForPlayer();
        } else if (filter.equals("industry") & applied) {
          filteredData = filteredData.filterForIndustry(value);
        } else if (filter.equals("country") & applied) {
          filteredData = filteredData.filterForCountry(value);
        } else if (filter.equals("state") & applied) {
          filteredData = filteredData.filterForState(value);
        } else if (filter.equals("city") & applied) {
          filteredData = filteredData.filterForCity(value);
        } else if (filter.equals("year") & applied) {
          yearFilterApplied = true;
          year = value;
        } else if (filter.equals("period") & applied) {
          period = value;
        }
      }

      if (yearFilterApplied) {
        filteredData = filteredData.filterForYear(year, period);
      }
      // all items loop successfully, return true with keys list
      // System.out.println(filteredData.fullData);
      return ResponseEntity.ok(new FilterResponse(true, filteredData.fullData));


    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    } catch (NumberFormatException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body("NumberFormatException: the year inputed was not an appropriate year."));
    }
  }

  @PostMapping("/query")
  public ResponseEntity query(@RequestHeader(value = "Authorization", required = true) String authToken,
      @RequestBody String requestBody){
    try {
      // Authenticating User
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the query endpoint.");
      // System.out.println(requestBody);
      // Deserializing the Json request body into a QueryObject
      QueryObject queryObject = QueryDeserializer.deserialize(requestBody);
      //System.out.println(queryObject.query);
      //System.out.println("number of active results: " + queryObject.activeResults.size());
      // Create new, temporary database of only active users
      DataBase activeData = new DataBase(queryObject.activeResults);
      //System.out.println("loaded active Results in successfully");
      // Instantiate querier
      Querier querier = new Querier(activeData);
      // Querying active results with query information
      HashMap<Integer, User> results = querier.query(queryObject.query);
      //System.out.println(results);
      return ResponseEntity.ok(new QueryResponse(true, results));

    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    } catch (Exception el) {
      System.out.println(el);
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(el));
    }
  }

  @GetMapping("/favs")
  public ResponseEntity myNetwork(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the favs endpoint");

      DataBase dataBase = new DataBase(serverState.usersDatabase.fullData);
      ArrayList<Integer> favs = currentUser.favorites;

      DataBase favUsers = dataBase.filterForFavs(favs);
      // System.out.println(favUsers);
      // all items loop successfully, return true with keys list
      // System.out.println(favUsers.fullData);
      return ResponseEntity.ok(new FavsResponse(true, favUsers.fullData));

    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @PostMapping("/otherUser")
  public ResponseEntity otherUser(@RequestHeader(value = "Authorization", required = true) String authToken,
      @RequestBody OtherUserInfo otherUserInfo) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the otherUser endpoint (user " + otherUserInfo.userId + ")");

      DataBase dataBase = new DataBase(serverState.usersDatabase.fullData);

      User userInfo = dataBase.fullData.get(Integer.parseInt(otherUserInfo.userId));
      //System.out.println(userInfo);
      return ResponseEntity.ok(new OtherUserInfoResponse(true, userInfo));

    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }


  @GetMapping("/filter-lists")  // Maps GET requests to /name
  public ResponseEntity getLists(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      User currentUser = authenticate(authToken);
      System.out.println(currentUser.name.firstName + " is accessing the filter-lists endpoint.");
      return ResponseEntity.ok(new FilterListsResponse(true, serverState.usersDatabase.countryStateMap, serverState.usersDatabase.usedIndustries, serverState.usersDatabase.usedClassYears));
    } catch (AuthErrorException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage()));
    }
  }

  @GetMapping("/logout")
  public ResponseEntity logout(@RequestHeader(value = "Authorization", required = true) String authToken) {
    try {
      this.serverState.logout(authToken);
      return ResponseEntity.ok(new PostResponse(true));
    } catch (UnsupportedOperationException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (ClassCastException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    } catch (NullPointerException e) {
      return (ResponseEntity.status(HttpStatusCode.valueOf(400)).body(e.getMessage()));
    }
  }






}