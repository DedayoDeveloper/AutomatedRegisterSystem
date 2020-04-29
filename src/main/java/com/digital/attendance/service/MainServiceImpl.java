/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.service;

import com.digital.attendance.aop.TrackTime;
import com.digital.attendance.mail.MailService;
import com.digital.attendance.model.*;
import com.digital.attendance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author adedayo
 */
@Service
public class MainServiceImpl implements MainInterface {


    @Autowired
    private UserRepository repository;

    @Autowired
    private UserClockTimeRepository clockinrepo;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    private PasswordEncoder passwordencoder;

    @Autowired
    MailService mailService;

    @Autowired
    private SupervisorAdminRepository supervisoradmin;

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private MapUserLocationRepository mapUserRepo;

    @Autowired
    private PushMailNotificationRepository pushMailNotificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);


    // BACKGROUND SERVICE TO AUTO-CLOCK OUT EVERY 12:00 AM DAILY FOR ANYONE WHO HAS NOT CLOCKED OUT FOR THAT DAY
    @Scheduled(cron = "0 0 23 ? * ?")
    public void systemAutoClockOutUser() throws ParseException {
        Date todayDate = new Date();
        String systemLOgOut = "SYSTEM CLOCK-OUT";
        int setClockOutSysytem = clockinrepo.autoSetClockOutSystem(systemLOgOut);
        String setTime = "24:00:00";
        int setTimeOutForThosWhoForgotToClockOut = clockinrepo.autoSetClockoutTime(setTime);
        List<String> getListOfUsers = clockinrepo.getListOfAllThoseWhoSystemClockedOut();
        int getNumberOfUser = getListOfUsers.size();
        logger.info("NUMBER OF SYSTEM CLOCKED OUT USERS FOR " + todayDate + " is :=== " + getNumberOfUser);
        for (String timeIn : getListOfUsers){
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(timeIn);
            Date date2 = format.parse(setTime);
            long difference = date2.getTime() - date1.getTime();
            long totalSecs = difference/1000;
            long hours = totalSecs / 3600;
            long minutes = (totalSecs % 3600) / 60;
            long seconds = totalSecs % 60;
            StringBuilder sb = new StringBuilder();
            sb.append(hours);
            sb.append(":");
            sb.append(minutes);
            sb.append(":");
            sb.append(seconds);
            int updateTimeSpent = clockinrepo.setAutoSetTimeCount(sb.toString(),timeIn);

        }

    }


    //ADMIN AUTHENTICATE USER FOR LOGIN

    @Override
    @TrackTime
    public MapUserLocation authenticateUser(String email, String medicalcenter) {
          User checkEmail = repository.findByEmail(email);
          if(checkEmail == null){
              throw new RuntimeException("No user found.");
          }
          Locations getLocation = locationRepo.findByMedicalcenter(medicalcenter);
          if(getLocation == null){
              throw new RuntimeException("No center found.");
          }
        int authenticateUser = repository.authenticateUser(email);
        MapUserLocation mapUser = new MapUserLocation();
        mapUser.setEmail(email);
        mapUser.setMedicalcenter(medicalcenter);
        mapUser.setLatitude(getLocation.getLatitude());
        mapUser.setLongitude(getLocation.getLongitude());
        mapUserRepo.save(mapUser);
        return mapUser;
    }


    
    // USER CLOCK OUT SERVICE
    @Override
    @TrackTime
    public UserClockTime clockOutUser(UserClockTime users) throws ParseException {
        UserClockTime user = clockinrepo.getExactUserToClockout(users.getEmail(), users.getLocation(), users.getTimein());
        if (user == null) {
            throw new RuntimeException("No user clocked in with this details.");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        // REASON FOR ADDING 1 HOUR IS BECAUSE THE SERVER TIME IS -1 HOUR
        LocalDateTime clockOutTime = now.plusHours(1);
        String timeout = dtf.format(clockOutTime);
        String timeIn = user.getTimein();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(timeIn);
        Date date2 = format.parse(timeout);
        long difference = date2.getTime() - date1.getTime();
        long totalSecs = difference/1000;
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(":");
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
//        logger.info("FULL TIME IS = " + hours + ":" + minutes + ":" + seconds);
//        int timecountt = clockinrepo.setTimeCount(users.getEmail(),users.getTimein(),users.getDate());
//        String timecount = Integer.toString(timecountt);
//        logger.info("HOUR DIFFERENCE = " + timecount);
        user.setTimeout(timeout);
        user.setTimespent(sb.toString());
        clockinrepo.save(user);
        return user;
    }


    
    

    // USER CLOCK IN SERVICE
    @Override
    @TrackTime
    public UserClockTime clockinTime(String email,String location) {
        logger.info("DEBUGGING!!");
        UserClockTime user = new UserClockTime();
        User getUserDetails = repository.findByEmail(email);
        if(getUserDetails == null){
            throw new RuntimeException("User does not exist");
        }
        String profession = getUserDetails.getUsertype();
        String firstname = getUserDetails.getFirstname();
        String lastname = getUserDetails.getLastname();

            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
            String today = formatter.format(date);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            // REASON FOR ADDING 1 HOUR IS BECAUSE THE DIGITAL OCEAN SERVER TIME IS -1 HOUR
            LocalDateTime clockInTime = now.plusHours(1);
            String timein = dtf.format(clockInTime);

            String getlocation = mapUserRepo.confirmClockInCenter(email, location);
            if (location == null) {
                throw new RuntimeException("You are not authorized to clock in this location.");
            }

            String latitude = mapUserRepo.confirmClockInCenterForLatitude(email, location);
            if (latitude == null) {
                throw new RuntimeException("You are not authorized to clock in this location.");
            }

            String longitude = mapUserRepo.confirmClockInCenterForLongitude(email, location);
            if (latitude == null) {
                throw new RuntimeException("You are not authorized to clock in this location.");
            }


            user.setDate(today);
            user.setTimein(timein);
            user.setLocation(getlocation);
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            user.setEmail(email);
            user.setProfession(profession);
            user.setFirstname(firstname);
            user.setLastname(lastname);

            clockinrepo.save(user);


            return user;
            }


    public void sendMailMessage(String email,String subject,String body){
        logger.info("GOT TO MAIL SENDER SERVICE");
        Mail mail = new Mail();
        mail.setMailFrom("mytestapp124@gmail.com");
        mail.setMailTo(email);
        mail.setMailSubject(subject);
        mail.setMailContent(body);
        mailService.sendEmail(mail);
    }

    // REGISTER NEW USER AND SEND EMAIL TOKEN TO EMAIL ADDRESS SERVICE
    @Override
    @TrackTime
    public String registerNewUserAccount(User accountDto) {

        //GENERATE EMAIL TOKEN
        String emailToken = String.valueOf(Math.random()).substring(2, 6);

        Date localDateTime = new Date();
        User user = new User();
        user.setFirstname(accountDto.getFirstname());
        user.setLastname(accountDto.getLastname());
        user.setEmail(accountDto.getEmail());
        User newUser = repository.findByEmail(accountDto.getEmail());
        logger.info("newUser = " + newUser);
        if(newUser != null){
            throw new RuntimeException("Email registered to an existing user.");
        }
        logger.info("user email = " + accountDto.getEmail());
        user.setAccountnumber(accountDto.getAccountnumber());
        user.setPhonenumber(accountDto.getPhonenumber());
        user.setAddress(accountDto.getAddress());
        user.setLocation(accountDto.getLocation());
        user.setPassword(passwordencoder.encode(accountDto.getPassword()));
        user.setUsertype(accountDto.getUsertype());
        user.setIdentification(accountDto.getIdentification());
        user.setGender(accountDto.getGender());
        user.setDateofbirth(accountDto.getDateofbirth());
        user.setBankname(accountDto.getBankname());
        user.setBvnnumber(accountDto.getBvnnumber());
        user.setDateregistered(localDateTime);
        user.setEmailtoken(emailToken);

        repository.save(user);

        // SEND EMAIL USING GMAIL SMTP SERVER DURING REGISTERATION
        String body = "Welcome to LongBridgeTech. Please find token in this mail to complete sign up " + emailToken;
        String subject = "Verify your account";
        sendMailMessage(accountDto.getEmail(),subject,body);


        return "User registered successfully";
    }

    @Override
    @TrackTime
    public String getEmailsForPushNotification(String medicalcenter, String subject,String body){

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");
        String newDate = format.format(date);

        List<String> getEmailForMessage = new ArrayList<>();
                getEmailForMessage = mapUserRepo.getAllEmailsForPushNotification(medicalcenter);
        if(getEmailForMessage == null){
            throw new RuntimeException("No users registered to this location.");
        }
        int emailSize = getEmailForMessage.size();
        for (String emails : getEmailForMessage){

            // SEND BULK EMAILS USING GMAIL SMTP SERVER
            sendMailMessage(emails,subject,body);
        }

        PushMailNotification mailNotification = new PushMailNotification();
        mailNotification.setMedicalcenter(medicalcenter);
        mailNotification.setContent(body);
        mailNotification.setSubject(subject);
        mailNotification.setDatesent(newDate);
        pushMailNotificationRepository.save(mailNotification);

        return "message sent successfully";
    }


    // VERIFY EMAIL TOKEN FROM USER
    @Override
    public String getEmailToken(String emailtoken) {
        String user = repository.getEmailToken(emailtoken);
        if (user == null) {
            throw new RuntimeException("Invalid token details.");
        }
        return "Email verification success";
    }


    // GET USERS WHO ARE NOT YET AUTHORIZED
    @Override
    @TrackTime
    public List<User> getUnauthorizedUserWhoHaveRegistered(){
        List<User> getUsers = repository.getUnauthorizedUsers();
        if(getUsers == null){
            throw new RuntimeException("No unauthorized users.");
        }
        return getUsers;
    }


    @Override
    @TrackTime
    public int getUnauthorizedUserWhoHaveRegisteredInCount(){
        int getUsers = repository.getUnauthorizedUsersInCount();
        return getUsers;
    }



    // USER LOGIN WITH EMAIL AND PASSWORD
    @Override
    @TrackTime
    public List<MapUserLocation>  signInUser (String email, String password) {
        User userbyemail = repository.findByEmail(email);
        if (userbyemail == null) {
            throw new RuntimeException("User does not exist.");
        }
        boolean passwordcheck = passwordencoder.matches(password, userbyemail.getPassword());
        if (passwordcheck == false) {
            throw new RuntimeException("Password mismatch.");
        }
        int authorized = userbyemail.getEnabled();
        if (authorized == 0 || authorized == 2) {
            throw new RuntimeException("Please contact your supervisor to authorize your profile.");
        }
        List<MapUserLocation> getUserLocationDetails = mapUserRepo.findByEmail(email);
        return getUserLocationDetails;
    }


    @Override
    public User adminSignIn(String username, String password){
        User adminUser = repository.findByEmail(username);
        String adminRole = adminUser.getUserrole();
        logger.info("UserRole = " + adminRole);
        if(adminUser.getUserrole().contains("user")){
            throw new RuntimeException("YOU ARE NOT AUTHORIZED TO SIGN IN");
        }
            boolean passwordcheck = passwordencoder.matches(password, adminUser.getPassword());
            if (passwordcheck == false) {
                throw new RuntimeException("Incorrect Password.");
            }
        return adminUser;
    }


    // SERVICE METHOD TO GET THE AMOUNT OF USERS CLOCKED IN ON PRESENT DAY
    @Override
    @TrackTime
    public String getUserClockedInToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String mydate = formatter.format(date);
        String userCount = clockinrepo.UserCount(mydate);
        return userCount;
    }


    // UPDATE USER PROFILE
    @Override
    @TrackTime
    public String updateUserProfileNew(String firstname, String lastname, String accountnumber, String email,
            String phonenumber, String address, String bankname, String location) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email user does not exist");
        }
        if (firstname != null) {
            user.setFirstname(firstname);
        }
        if (lastname != null) {
            user.setLastname(lastname);
        }
        if (accountnumber != null) {
            user.setAccountnumber(accountnumber);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (phonenumber != null) {
            user.setPhonenumber(phonenumber);
        }
        if (address != null) {
            user.setAddress(address);
        }
        if (bankname != null) {
            user.setBankname(bankname);
        }
        if (location != null) {
            user.setLocation(location);
        }
        repository.save(user);
        return "User successfully updated";
    }

    // ADMIN DEACTIVATE USER
    @Override
    @TrackTime
    public boolean deactivateUser(User user) {
        boolean value = false;
        int confirmUser = repository.deactivateUser(user.getEmail());
        if(confirmUser == 1){
            value = true;
        }
        return value;
    }


    @Override
    @TrackTime
    public User checkIfUserExists(String email) {
        User confirmEmail = repository.findByEmail(email);
        if(confirmEmail == null){
            throw new RuntimeException("User email does not exist.");
        }
        return confirmEmail;
    }

    // GET THE TIME DIFFERENCE BETWEEN USER CLOCK IN AND CLOCK OUT
    @Override
    @TrackTime
    public String getTimeCount(UserClockTime user) {
        String getTimeCount = clockinrepo.getTimeCount(user.getEmail());
        if(getTimeCount == null){
            throw new RuntimeException("No user record found.");
        }
        return getTimeCount;
    }

    // SERVICE TO DOWNLOAD USER'S ATTENDANCE IN DIFFERENT FORMATS(PDF,CSV,EXCEL)
    @Override
    @TrackTime
    public List<UserClockTime> downloadUserClockData(String location) {
        List<UserClockTime> getUserDetails = clockinrepo.downloadUserAttendance(location);
        if(getUserDetails == null){
            throw new RuntimeException("No user record found.");
        }
        return getUserDetails;
    }

    // GET USER ATTENDANCE HISTORY
    @Override
    @TrackTime
    public List<UserClockTime> getUserHistory(String email) {
        List<UserClockTime> user = clockinrepo.getAllUserHistory(email);
        if(user == null){
            throw new RuntimeException("No user record found.");
        }
        return user;
    }

    // PASSWORD RESET TOKEN SENT TO EMAIL ADDRESS
    @Override
    @TrackTime
    public String passwordResetToken(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email does not exist.");
        }
        String updatePasswordToken = String.valueOf(Math.random()).substring(2, 6);
        int updatePassword = repository.updatePasswordToken(updatePasswordToken, email);
        String subject = "Password Reset";
        String body = "Please find token to reset password " + updatePasswordToken;
        sendMailMessage(user.getEmail(),subject,body);
        return "Password reset token successfully sent";
    }

// SERVICE TO CONFIRM PASSWORD RESET TOKEN BEFORE RESET ACTION CAN BE COMPLETED
    @Override
    @TrackTime
    public String confirmToken(String updatepasswordtoken, String email) {
        User users = repository.confirmPasswordToken(updatepasswordtoken,email);
        if (!users.getEmail().equals(email)) {
            throw new RuntimeException("Invalid token details.");
        }
        return "Token verification successful";
    }

// RESET PASSWORD SERVICE
    @Override
    @TrackTime
    public String passwordReset(User user) {
        int updatePassword = repository.passwordReset(passwordencoder.encode(user.getPassword()), user.getEmail());
        if(updatePassword == 0){
            throw new RuntimeException("Invalid email");
        }
        return "Password reset successful";
    }

    // SUPER ADMIN CREATES SUPERVISOR ADMIN
    @Override
    @TrackTime
    public SupervisorAdmin createSupervisorAdmin(SupervisorAdmin sa) {
        SupervisorAdmin supervisor = new SupervisorAdmin();
        supervisor.setUsername(sa.getUsername());
        SupervisorAdmin user = supervisoradmin.findByEmail(sa.getEmail());
        if(user != null){
            throw new RuntimeException("Email registered to an existing supervisor");
        }
        supervisor.setEmail(sa.getEmail());
        supervisor.setLocation(sa.getLocation());
        supervisor.setPhonenumber(sa.getPhonenumber());
        supervisor.setPassword(passwordencoder.encode(sa.getPassword()));
        supervisoradmin.save(supervisor);
        return supervisor;
    }

    // SUPERVISOR ADMIN LOGIN SERVICE
    @Override
    @TrackTime
    public SupervisorAdmin loadSupervisorAdminAccountByEmail(String email, String password) {
        SupervisorAdmin userbyemail = supervisoradmin.findByEmail(email);
        if (userbyemail == null) {
            throw new RuntimeException("User does not exist.");
        }

        boolean passwordcheck = passwordencoder.matches(password, userbyemail.getPassword());

        if (passwordcheck == false) {
            throw new RuntimeException("Password mismatch.");
        }

        return userbyemail;

    }

    // SEARCH USER BY KEYWORD
    @Override
    @TrackTime
    public List<User> searchForUserUsingAnyKeyword(String search) {
//        User user = repository.searchUserByEmail(users.getEmail());
     List<User> user = repository.findByEmailOrFirstnameOrLastnameOrPhonenumberOrLocationOrUsertype(search,search,search,search,search,search);
        logger.info("USER SIZE: " + user.size());
     if (user.size() == 0) {
            throw new RuntimeException("No user found.");

        }
        return user;
    }

    // SEARCH FOR USER ATTENDANCE BY EMAIL
    @Override
    @TrackTime
    public UserClockTime searchUserByEmailForAttendance(UserClockTime users) {
        UserClockTime user = clockinrepo.searchUserByEmailForAttendance(users.getEmail());
        if (user == null) {
            throw new RuntimeException("User has no data.");
        }
        return user;
    }

    // DOWNLOAD USER ATTENDANCE 
    @Override
    @TrackTime
    public List<UserClockTime> downloadUserAttendanceByEmail(String email) {
        List<UserClockTime> getUserDetails = clockinrepo.downloadEachUserAttendanceByEmail(email);
        if (getUserDetails == null) {
            throw new RuntimeException("No data found.");
        }
        return getUserDetails;
    }

    // GET LOCATION DETAILS
    @Override
    @TrackTime
    public Locations getLocationDetails(String medicalcenter) {
        Locations location = locationRepo.findByMedicalcenter(medicalcenter);
        if (location == null) {
            throw new RuntimeException("Medical-Center does not exists.");
        }
        return location;
    }


    // GET USER LOCATION 
    @Override
    @TrackTime
    public List<MapUserLocation> getUserLocations(String email) {
        List<MapUserLocation> getLocation = mapUserRepo.findByEmail(email);
        if (getLocation == null) {
            throw new RuntimeException("Does not exists.");
        }
        return getLocation;
    }



    
    @Override
    @TrackTime
    public String updateUserLocation(String medicalcenter, long id) {
        String value = "failed";
        Locations getLocationDetails = locationRepo.findByMedicalcenter(medicalcenter);
        if(getLocationDetails == null){
            throw new RuntimeException("No location found.");
        }
        int updateUserLocation = mapUserRepo.updateUserLocation(getLocationDetails.getLongitude(), getLocationDetails.getLatitude(), medicalcenter, id);
        if (updateUserLocation > 0) {
            value = "Update complete";
        }
        return value;
    }


    // SERVICE TO COUNT HOW MANY USERS ARE ON THIS PLATFORM-----------------------------------------
        @Override
        @TrackTime
        public int getTotalAmountOfRegisteredUsersOnThisApplication(){
        int getTotal = repository.getNumberOfUserOnThePlatform();
        return getTotal;
        }


        @Override
        @TrackTime
        public MapUserLocation mapUserToLocation(String email,String medicalcenter){
        MapUserLocation confirmUserLocationDetails = mapUserRepo.findByEmailAndMedicalcenter(email,medicalcenter);
        if (confirmUserLocationDetails != null){
            throw new RuntimeException("Email already registered to location.");
        }
        Locations getMedicalCenter = locationRepo.findByMedicalcenter(medicalcenter);
        if(getMedicalCenter == null){
            throw new RuntimeException("Medical-Center not exists.");
        }
            User checkEmail = repository.findByEmail(email);
        if (checkEmail == null){
            throw new RuntimeException("Email does not exists.");
        }

        MapUserLocation mapUser = new MapUserLocation();
        mapUser.setEmail(email);
        mapUser.setMedicalcenter(medicalcenter);
        mapUser.setLatitude(getMedicalCenter.getLatitude());
        mapUser.setLongitude(getMedicalCenter.getLongitude());
        mapUserRepo.save(mapUser);

        return mapUser;
        }






}
