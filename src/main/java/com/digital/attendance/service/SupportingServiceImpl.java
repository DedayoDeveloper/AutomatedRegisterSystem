package com.digital.attendance.service;

import com.digital.attendance.aop.TrackTime;
import com.digital.attendance.mail.MailService;
import com.digital.attendance.model.*;
import com.digital.attendance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupportingServiceImpl implements  SupportingInterface{

    private static final Logger logger = LoggerFactory.getLogger(SupportingServiceImpl.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserClockTimeRepository userClockTimeRepository;

    @Autowired
    private MapUserLocationRepository mapUserLocationRepository;

    @Autowired
    private PushMailNotificationRepository pushMailNotificationRepository;

    @Autowired
    private SupervisorAdminRepository supervisorAdminRepository;

    @Autowired
    MailService mailService;



    public void sendMailMessage(String email,String subject,String body){
        logger.info("GOT TO MAIL SENDER SERVICE");
        Mail mail = new Mail();
        mail.setMailFrom("mytestapp124@gmail.com");
        mail.setMailTo(email);
        mail.setMailSubject(subject);
        mail.setMailContent(body);
        mailService.sendEmail(mail);
    }

    @Override
    @TrackTime
    public List<Locations> getAllLocations(){
        List<Locations> getLocations = locationRepository.findAll();
        return getLocations;
    }

    @Override
    @TrackTime
    public User getUserDetails(long id){
        User getUserDetails= userRepository.findById(id);
        if(getUserDetails == null){
            throw new RuntimeException("No user found");
        }
        return getUserDetails;
    }

    @Override
    @TrackTime
    public String adminCreateProfession(String profession)

    {
                    String newProfession = profession.toUpperCase();
                    UserTypes saveUser = new UserTypes();

                    String checkProfessionName = userTypeRepository.getUserType(profession);
                    if (checkProfessionName == null){
                        saveUser.setUsertype(profession.toUpperCase());
                        UserTypes createProfession = userTypeRepository.save(saveUser);
                    } else if(checkProfessionName.equals(newProfession)){
                        throw new RuntimeException("Profession Exists.");
                    }

                    return "Profession saved Successfully";
    }

    @Override
    @TrackTime
    public List<UserTypes> getAllProfession(){
        List<UserTypes> getAllProfession = userTypeRepository.findAll();
        return getAllProfession;
    }

    @Override
    @TrackTime
    public List<String> getListOfUserClockInDetailsForCurrentDay(String email){
        List<String> getDetails = userClockTimeRepository.getUserClockInDetailsForAdmin(email);
        if (getDetails == null){
            throw new RuntimeException("No details found.");
        }
        return getDetails;
    }



    @Override
    @TrackTime
    public List<User> getAllUsersInALocations(String medicalcenter){
        List<User> userDetails = new ArrayList<>();
        List<MapUserLocation>  getUsersMappedToLocation = mapUserLocationRepository.findByMedicalcenter(medicalcenter);
        if(getUsersMappedToLocation == null){
            throw new RuntimeException("No Users Registered to Location.");
        }
            int getListSize = getUsersMappedToLocation.size();
        try {
            for (int i = 0; i <= getListSize; i++) {
                String email = getUsersMappedToLocation.get(i).getEmail();
                User getUserDetails = userRepository.findByEmail(email);
                getUserDetails.setPassword(null);
                userDetails.add(getUserDetails);
            }
        } catch (IndexOutOfBoundsException a){

        }
        return userDetails;
    }



    @Override
    @TrackTime
    public List<PushMailNotification> getMailMessagesSentToLocation(String medicalcenter){
        List<PushMailNotification> getMessages = pushMailNotificationRepository.findByMedicalcenter(medicalcenter);
        if(getMessages.isEmpty()){
            throw new RuntimeException("No messages sent to this location");
        }
        return getMessages;
    }


    @Override
    @TrackTime
    public List<PushMailNotification> getAllMessagesSentToAlllocations(){
        List<PushMailNotification> getMessageList = pushMailNotificationRepository.findAll(Sort.by("id").descending());
        if(getMessageList.isEmpty()){
            throw new RuntimeException("No messages found");
        }
        return getMessageList;
    }


    @Override
    @TrackTime
    public List<UserClockTime> getListOfAllUsersAttendanceInAlocation(String location){
        List<UserClockTime> getDetails = userClockTimeRepository.findByLocation(location);
        if(getDetails.isEmpty()){
            throw new RuntimeException("No users registered to this location");
        }
        return getDetails;
    }

    @Override
    @TrackTime
    public List<SupervisorAdmin> getListOfAllsupervisors(){
        List<SupervisorAdmin> getList = supervisorAdminRepository.getListOfAllSupervisors();
        if(getList.isEmpty()){
            throw new RuntimeException("No supervisors found");
        }
        return getList;
    }

    @Override
    public List<UserClockTime> getAllUsersClockedInForToday(){
        List<UserClockTime> getListOfUsers = userClockTimeRepository.getListOfClockedInUsersForToday();
        if(getListOfUsers == null){
            throw new RuntimeException("No users clocked in today");
        }
        return getListOfUsers;
    }


        @Override
        public List<UserClockTime> getFilterSearch(String profession,String location, String startDate, String endDate){
            List<UserClockTime> getList = null;
            if(profession.isEmpty() && location.isEmpty()) {
                getList  = userClockTimeRepository.filterSearchOne(startDate, endDate);
            } else if (profession.isEmpty() && !location.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                getList = userClockTimeRepository.filterSearchTwo(location, startDate, endDate);
            } else if(location.isEmpty() && !profession.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()){
                getList = userClockTimeRepository.filterSearchThree(profession,startDate,endDate);
            } else if(startDate.isEmpty() && endDate.isEmpty() && !profession.isEmpty() && !location.isEmpty()){
                getList = userClockTimeRepository.filterSearchFour(profession,location);
            } else if(!profession.isEmpty() && !location.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()){
                getList = userClockTimeRepository.filterSearchFive(profession,location,startDate,endDate);
            } else if(location.isEmpty() && startDate.isEmpty() && endDate.isEmpty() && !profession.isEmpty()){
                getList = userClockTimeRepository.filterSearchSix(profession);
            } else if(profession.isEmpty() && startDate.isEmpty() && endDate.isEmpty() && !location.isEmpty()){
                getList = userClockTimeRepository.filterSearchSeven(location);
            }
            return getList;
        }


        @Override
        public List<UserClockTime> getListOfAllUsersClockedIn(){
            List<UserClockTime> getList = userClockTimeRepository.findAll(Sort.by("id").descending());
            return getList;
        }

        @Override
        public String getListOfAllEmailsAndSendGeneralEmails(String subject, String content){
            List<String> listOfEmails = new ArrayList<>();
            listOfEmails = userRepository.getListOfAllEmailInApplication();
            int getListSize = listOfEmails.size();
            for (String emails : listOfEmails){

                // SEND BULK EMAILS USING GMAIL SMTP SERVER
                sendMailMessage(emails,subject,content);
            }
           return "Message sent successfully";
        }

        @Override
        public List<User> getListOfDeactivatedUsers(){
         List<User> getListOfDeactivatedUsers = userRepository.getListOfDeactivatedUsers();
         if(getListOfDeactivatedUsers.isEmpty()){
             throw new RuntimeException("No deactivated users");
         }
         return getListOfDeactivatedUsers;
        }

        @Override
        public int getCountOfdeactivatedUsers(){
           int getCountOfUsers = userRepository.getCountOfDeactivatedUsers();
           return getCountOfUsers;
        }


        @Override
        public String reactivateUserWhoHasBeenDeactivated(String email){
           String value = null;
            int reactivateUser = userRepository.reactivateUser(email);
            if(reactivateUser > 0){
                 value = "User successfully reactivated";
            }
            return value;
        }


        @Override
        public List<Object> getTotalHours(String startdate, String enddate){
            List<Object> getTotalHours = userClockTimeRepository.getTotalHoursWorked(startdate,enddate);
            return getTotalHours;
        }

        @Override
        public String createNewLocation(String location,String latitude,String longitude){
            Locations locations = new Locations();
            locations.setMedicalcenter(location);
            locations.setLatitude(latitude);
            locations.setLongitude(longitude);
            locationRepository.save(locations);
            return "Location successfully created";
        }




}
