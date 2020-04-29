package com.digital.attendance.controller;

import com.digital.attendance.model.*;
import com.digital.attendance.response.ApiResponse;
import com.digital.attendance.service.MainInterface;
import com.digital.attendance.service.SupportingInterface;
import com.digital.attendance.service.myUserDetailsService;
import com.digital.attendance.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SecondController {

    private static final Logger logger = LoggerFactory.getLogger(SecondController.class);

    @Autowired
    private SupportingInterface supportingInterface;

    @Autowired
    private MainInterface mainInterface;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private myUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwttokenutil;

    @GetMapping("/ListOfLocations")
    public ApiResponse<List<Locations>> getListOfAllLocations(){
        ApiResponse<List<Locations>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getAllLocations());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        return u;
    }


    @GetMapping("/getUserDetails/{id}")
    public ApiResponse<User> getUserDetails(@PathVariable("id") long id){
        ApiResponse<User> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getUserDetails(id));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        return u;
    }


    @PostMapping("createProfessionForUsers")
    public ApiResponse<String> createProfessionForUsers(@RequestBody UserTypes userTypes){
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(supportingInterface.adminCreateProfession(userTypes.getUsertype()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        return u;
    }

    @GetMapping("getAllProfessions")
    public ApiResponse<List<UserTypes>> getAllUserTypes(){
        ApiResponse<List<UserTypes>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getAllProfession());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    @PostMapping("/getUserClockInDetailsForCurrentDay")
    public ApiResponse<List<String>> getUserCurrentDayClockInDetails(@RequestBody UserClockTime userClockTime){
        ApiResponse<List<String>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfUserClockInDetailsForCurrentDay(userClockTime.getEmail()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }


    @PostMapping("/getUsersForSpecificLocation")
    public ApiResponse<List<User>> getListOfUsersForLocation(@RequestBody MapUserLocation mapUserLocation){
        ApiResponse<List<User>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getAllUsersInALocations(mapUserLocation.getMedicalcenter()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;

    }

    @PostMapping("/getMailNotificationSentToAllUserInLocation")
    public ApiResponse<List<PushMailNotification>> getListOfMessagesSentToALocation(@RequestBody PushMailNotification center){
        ApiResponse<List<PushMailNotification>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getMailMessagesSentToLocation(center.getMedicalcenter()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }


    @GetMapping("/getAllMessagesSent")
    public ApiResponse<List<PushMailNotification>> getListOfAllLocation(){
        ApiResponse<List<PushMailNotification>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getAllMessagesSentToAlllocations());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }



    @PostMapping("/getUserAttendanceDetailsToDownloadInCsv")
    public ApiResponse<List<UserClockTime>> getLocationdetailsToDownloadincsv(@RequestBody UserClockTime getListOfUsers){
        ApiResponse<List<UserClockTime>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfAllUsersAttendanceInAlocation(getListOfUsers.getLocation()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }

    @GetMapping("/getListOfSupervisorAdmin")
    public ApiResponse<List<SupervisorAdmin>> getListOfSupervisorscreated(){
        ApiResponse<List<SupervisorAdmin>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfAllsupervisors());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }

    @GetMapping("/getListOfAllUserClockedIn_Today")
    public ApiResponse<List<UserClockTime>> getListOfAllUsersClockedInToday(){
        ApiResponse<List<UserClockTime>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getAllUsersClockedInForToday());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;

    }

    @PostMapping("/filterSearchForAttendance")
    public ApiResponse<List<UserClockTime>>getFilteredSearchForUserAttendance(@RequestBody ObjectNode filterSearch){
        ApiResponse<List<UserClockTime>> u = new ApiResponse<>();
        String profession = filterSearch.get("profession").asText();
        String location = filterSearch.get("location").asText();
        String startDate = filterSearch.get("startdate").asText();
        String endDate = filterSearch.get("enddate").asText();
        u.setResponse(supportingInterface.getFilterSearch(profession,location,startDate,endDate));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }

    @GetMapping("/getListOfAllUsersClockedInApplication")
    public ApiResponse<List<UserClockTime>> getListOfAllUsersClockedInApplication(){
        ApiResponse<List<UserClockTime>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfAllUsersClockedIn());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }


    @PostMapping("/sendGeneralMailToAllUsers")
    public ApiResponse<String> sendEmailMessageToAllUsersInApplication(@RequestBody PushMailNotification sendMail){
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfAllEmailsAndSendGeneralEmails(sendMail.getSubject(),sendMail.getContent()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }


    @GetMapping("/getListOfDeactivatedUsers")
    public ApiResponse<List<User>> getListODeactivatedUsers(){
        ApiResponse<List<User>> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getListOfDeactivatedUsers());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }

    @GetMapping("/getCountOfDeactivatedUsers")
    public ApiResponse<Integer> getCountOfDeactivatedUsers(){
        ApiResponse<Integer> u = new ApiResponse<>();
        u.setResponse(supportingInterface.getCountOfdeactivatedUsers());
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }

    @PostMapping("/reactivateUserWhoHasBeenDeactivated")
    public ApiResponse<String> reactivateUser(@RequestBody User user){
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(supportingInterface.reactivateUserWhoHasBeenDeactivated(user.getEmail()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }


    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {
        try {
            authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),  authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Wrong credentials");
        }
        String email = authenticationRequest.getEmail();
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new RuntimeException("User does not exist.");
        }
        User locations = mainInterface.adminSignIn(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        try{
            ObjectMapper mapper = new ObjectMapper();
            logger.info(mapper.writeValueAsString(userDetails));
        }catch (Exception e){
        }
        final String jwt = jwttokenutil.generateToken(userDetails);
        logger.info("USERNAME " + userDetails);
        return ResponseEntity.ok( new AdminLoginResponse(jwt));
    }





    @PostMapping("/getTotalHoursOfTimeSpent")
    public ApiResponse<List<Object>> getTotalHoursWorked(@RequestBody ObjectNode searchDate){
        ApiResponse<List<Object>> u = new ApiResponse<>();
        String startDate = searchDate.get("startdate").asText();
        String endDate = searchDate.get("enddate").asText();
        u.setResponse(supportingInterface.getTotalHours(startDate,endDate));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }


    @PostMapping("/createNewLocation")
    public ApiResponse<String> createNewLOcations(@RequestBody Locations locations){
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(supportingInterface.createNewLocation(locations.getMedicalcenter(),locations.getLatitude(),locations.getLongitude()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("success");
        u.setResponsecode("00");
        return u;
    }




}
