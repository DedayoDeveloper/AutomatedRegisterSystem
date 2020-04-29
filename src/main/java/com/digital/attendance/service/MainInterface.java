/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.service;

import com.digital.attendance.model.*;

import java.text.ParseException;
import java.util.List;

/**
 *
 * @author oreoluwa
 */
public interface MainInterface {

    public MapUserLocation authenticateUser(String email, String medicalcenter);
   public UserClockTime clockOutUser(UserClockTime users) throws ParseException;
//   public List<User> getAllUser();
public UserClockTime clockinTime(String email,String location);
    public String registerNewUserAccount(User accountDto);
    public List<MapUserLocation>  signInUser (String email, String password) ;
    public User adminSignIn(String username, String password);
    public String getUserClockedInToday();
    public boolean deactivateUser(User user);
    public User checkIfUserExists(String email);
    public String getTimeCount(UserClockTime user);
    public List<UserClockTime> downloadUserClockData(String location);
    public String updateUserProfileNew(String firstname, String lastname, String accountnumber, String email, 
               String phonenumber, String address, String bankname, String location);
    public String getEmailToken(String emailtoken);
    public List<UserClockTime> getUserHistory(String email);
    public String passwordResetToken(String email);
    public String confirmToken(String updatepasswordtoken, String email);
    public String passwordReset(User user);
    public SupervisorAdmin createSupervisorAdmin(SupervisorAdmin sa);
    public SupervisorAdmin loadSupervisorAdminAccountByEmail(String email,String password);
    public List<User> searchForUserUsingAnyKeyword(String search);
    public UserClockTime searchUserByEmailForAttendance(UserClockTime users);
    public List<UserClockTime> downloadUserAttendanceByEmail(String email);
    public Locations getLocationDetails(String medicalcenter);
//    public MapUserLocation mapUserToLocation(String email,String medicalcenter, String latitude, String longitude);
    public List<MapUserLocation> getUserLocations(String email);
    public String updateUserLocation(String medicalcenter, long id);
    public List<User> getUnauthorizedUserWhoHaveRegistered();
    public String getEmailsForPushNotification(String medicalcenter, String subject,String body);
    public int getTotalAmountOfRegisteredUsersOnThisApplication();
    public MapUserLocation mapUserToLocation(String email,String medicalcenter);
    public int getUnauthorizedUserWhoHaveRegisteredInCount();
    public void systemAutoClockOutUser() throws ParseException;
}
