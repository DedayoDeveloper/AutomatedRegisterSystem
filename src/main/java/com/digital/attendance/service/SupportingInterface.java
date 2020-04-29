package com.digital.attendance.service;

import com.digital.attendance.model.*;

import java.util.List;

public interface SupportingInterface {

    public List<Locations> getAllLocations();

    public User getUserDetails(long id);
    public String adminCreateProfession(String profession);
    public List<UserTypes> getAllProfession();
    public List<String> getListOfUserClockInDetailsForCurrentDay(String email);
    public List<User> getAllUsersInALocations(String medicalcenter);
    public List<PushMailNotification> getMailMessagesSentToLocation(String medicalcenter);
    public List<PushMailNotification> getAllMessagesSentToAlllocations();
    public List<UserClockTime> getListOfAllUsersAttendanceInAlocation(String location);
    public List<SupervisorAdmin> getListOfAllsupervisors();
    public List<UserClockTime> getAllUsersClockedInForToday();
    public List<UserClockTime> getFilterSearch(String profession,String location, String startDate, String endDate);
    public List<UserClockTime> getListOfAllUsersClockedIn();
    public String getListOfAllEmailsAndSendGeneralEmails(String subject, String body);
    public List<User> getListOfDeactivatedUsers();
    public int getCountOfdeactivatedUsers();
    public String reactivateUserWhoHasBeenDeactivated(String email);
    public List<Object> getTotalHours(String startdate, String enddate);
    public String createNewLocation(String location,String latitude,String longitude);
}
