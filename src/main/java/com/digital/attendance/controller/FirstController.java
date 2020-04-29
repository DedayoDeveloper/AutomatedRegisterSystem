/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.controller;

import com.digital.attendance.model.*;
import com.digital.attendance.repository.LocationRepository;
import com.digital.attendance.repository.UserClockTimeRepository;
import com.digital.attendance.repository.UserRepository;
import com.digital.attendance.response.ApiResponse;
import com.digital.attendance.service.DownloadService;
import com.digital.attendance.service.myUserDetailsService;
import com.digital.attendance.utils.GeneratePdfReport;
import com.digital.attendance.utils.JwtUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.digital.attendance.service.MainInterface;

/**
 *
 * @author adedayo
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class FirstController {

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private MainInterface userinterface;

    @Autowired
    private UserClockTimeRepository userclock;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private myUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwttokenutil;

    @Autowired
    DownloadService downloadservice;

    @Autowired
    private UserRepository repository;

    @Autowired
    private GeneratePdfReport pdfreport;

    @Autowired
    private LocationRepository locationsRepo;


    private static final Logger logger = LoggerFactory.getLogger(FirstController.class);

    // ENDPOINT TO GET WEBTOKEN AND SIGN IN USER
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {
        try {
            authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),  authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Wrong credentials");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        if (userDetails == null) {
            throw new RuntimeException("User does not exist.");
        }
        List<MapUserLocation> locations = userinterface.signInUser(authenticationRequest.getEmail(),authenticationRequest.getPassword());

        User userDetail = repository.findByEmail(authenticationRequest.getEmail());
        userDetail.setPassword(null);
        userDetail.setEmailtoken(null);
        userDetail.setUpdatepasswordtoken(null);
        userDetail.setDateofbirth(null);

        try{
            ObjectMapper mapper = new ObjectMapper();
            logger.info(mapper.writeValueAsString(userDetails));
        }catch (Exception e){

        }
        final String jwt = jwttokenutil.generateToken(userDetails);
        logger.info("USERNAME " + userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt,locations,userDetail));
    }





    // CREATE A USER AND STORE IN DB

    @PostMapping("/register")
    public ApiResponse<String> userRegisteration(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.registerNewUserAccount(user));
        u.setStatus(HttpStatus.CREATED);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }



    //AUTHENTICATE USER BEFORE ACCESS TO LOG IN

    @PutMapping("/authenticateuser")
    public ApiResponse<String> authenticateUserRegistered(@RequestBody ObjectNode user) {
        ApiResponse<String> u = new ApiResponse<>();
            try {
                String email = user.get("email").asText();
                JsonNode objectMedicalCenter = user.get("medicalcenter");
                JsonNode getCount = user.get("count");
                int count = getCount.asInt();
                for (int i = 0; i <= count; i++) {
                    String medicalCenter = objectMedicalCenter.get(i).asText();
                    userinterface.authenticateUser(email, medicalCenter);
                }
            } catch (NullPointerException n){}
            u.setResponse("User succesfully authorized");
            u.setStatus(HttpStatus.OK);
            u.setMessage("Success");
            u.setResponsecode("00");

        return u;
    }


    // GET USERS WHO HAVE REGISTERED AND ARE EXPECTING AUTHORIZATION FROM ADMIN

    @GetMapping("/getunauthorizeduser")
    public ApiResponse<List<User>> getUserWhoHaveSentRequestAndExpectingAuthorizaton(){
        ApiResponse<List<User>> u = new ApiResponse<>();
        u.setResponse(userinterface.getUnauthorizedUserWhoHaveRegistered());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    @GetMapping("/getunauthorizedUser_IN_COUNT")
    public ApiResponse<Integer> getUserWhoHaveSentRequestAndExpectingAuthorizatonInCount(){
        ApiResponse<Integer> u = new ApiResponse<>();
        u.setResponse(userinterface.getUnauthorizedUserWhoHaveRegisteredInCount());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }




    // EMAIL VERIFICATION

    @PostMapping("/verifyemail")
    public ApiResponse<String> verifyEmailToken(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.getEmailToken(user.getEmailtoken()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }


    // GET ALL USERS FROM DB

    @GetMapping("/allusers")
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> u = new ApiResponse<>();
        u.setResponse(repository.findAll(Sort.by("id").descending()));
        u.setMessage("Success");
        u.setStatus(HttpStatus.OK);
        u.setResponsecode("00");
        return u;
    }


    // CLOCK IN SYSTEM FOR USER

    @PostMapping("/clockintime")
    public ApiResponse<UserClockTime> clockInTime(@RequestBody UserClockTime clockin) {
        ApiResponse<UserClockTime> u = new ApiResponse<>();
        logger.info("EMAIL = " + clockin.getEmail());
        u.setResponse(userinterface.clockinTime(clockin.getEmail(),clockin.getLocation()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    //CLOCK OUT TIME FOR USER

    @PutMapping("/clockouttime")
    public ApiResponse<UserClockTime> userClockOutTime(@RequestBody UserClockTime clockin) throws ParseException {
        ApiResponse<UserClockTime> u = new ApiResponse<>();
        u.setResponse(userinterface.clockOutUser(clockin));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }


    // GET CLOCK IN USERS FOR TODAY

    @GetMapping("/getclockedinuserstoday")
    public ApiResponse<String> getUserClockedInToday() {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.getUserClockedInToday());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // UPDATE USER PROFILE

    @PutMapping("/updateuser")
    public ApiResponse<String> updateUser(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.updateUserProfileNew(user.getFirstname(), user.getLastname(), user.getAccountnumber(), user.getEmail(), user.getPhonenumber(),
                user.getAddress(), user.getBankname(), user.getLocation()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // DELETE USER PROFILE

    @PutMapping("/deactivateUser")
    public ApiResponse<String> deactivateUser(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        userinterface.deactivateUser(user);
        u.setResponse("User successfully deactivated");
        u.setStatus(HttpStatus.GONE);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    //GET HOW MANY HOURS WORKED BY A USER

    @PostMapping("/gettimecount")
    public ApiResponse<String> getUserTimeCount(@RequestBody UserClockTime user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.getTimeCount(user));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // DOWNLOAD USER CLOCK IN / CLOCK OUT DETAILS IN EXCEL

    @PostMapping(value = "/downloadAttendanceInExcel")
    public ResponseEntity<InputStreamResource> excelCustomersReport(@RequestBody UserClockTime user) throws IOException {
        List<UserClockTime> users = (List<UserClockTime>) userinterface.downloadUserClockData(user.getLocation());
        ByteArrayInputStream in = DownloadService.usersAttendanceToExcel(users);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    // DOWNLOAD USER CLOCK IN / CLOCK OUT DETAILS IN CSV

    @PostMapping("/downloadAttendanceInCsv")
    public String downloadCSV(@RequestBody UserClockTime user, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=users.csv");
        List<UserClockTime> users = (List<UserClockTime>) userinterface.downloadUserClockData(user.getLocation());
        downloadservice.writeObjectToCSV(response.getWriter(), users);
        return "File successfully downloaded";
    }

    // DOWNLOAD USER CLOCK IN / CLOCK OUT LIST FOR ALL USERS IN PDF BASED ON LOCATION

    @RequestMapping(value = "/downloadattendanceinpdf/{location}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> Report(@PathVariable("location") String location) {

        List<UserClockTime> downloadUser = (List<UserClockTime>) userclock.downloadUserAttendance(location);

        ByteArrayInputStream bis = pdfreport.userReport(downloadUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=usersreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    // GET USER HISTORY

    @PostMapping("/getuserhistory")
    public ApiResponse<List<UserClockTime>> getUserHistory(@RequestBody UserClockTime user) {
        ApiResponse<List<UserClockTime>> u = new ApiResponse<>();
        u.setResponse(userinterface.getUserHistory(user.getEmail()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }


    // PASSWORD RESET  TOKEN SENT TO EMAIL FOR CONFIRMATION

    @PostMapping("/passwordresettoken")
    public ApiResponse<String> passwordResetSendTokenToEmail(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.passwordResetToken(user.getEmail()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    //PASSWORD TOKEN CONFIRMATION

    @PostMapping("/confirmpasswordtoken")
    public ApiResponse<String> confirmPasswordToken(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.confirmToken(user.getUpdatepasswordtoken(),user.getEmail()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    //PASSWORD RESET

    @PutMapping("/updatepassword")
    public ApiResponse<String> passwordReset(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.passwordReset(user));
        u.setMessage("Success");
        u.setStatus(HttpStatus.OK);
        u.setResponsecode("00");
        return u;
    }

    // SUPER ADMIN TO CREATE SUPERVISOR ADMIN

    @PostMapping("/createsupervisoradmin")
    public ApiResponse<SupervisorAdmin> createSupervisorAdmin(@RequestBody SupervisorAdmin supervisor) {
        ApiResponse<SupervisorAdmin> u = new ApiResponse<>();
        u.setResponse(userinterface.createSupervisorAdmin(supervisor));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // LOG IN FOR SUPERVISOR ADMIN

    @PostMapping("supervisorlogin")
    public ApiResponse<SupervisorAdmin> supervisorLogin(@RequestBody SupervisorAdmin supervisor) {
        ApiResponse<SupervisorAdmin> u = new ApiResponse<>();
        u.setResponse(userinterface.loadSupervisorAdminAccountByEmail(supervisor.getEmail(), supervisor.getPassword()));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // SUPERVISOR ADMIN TO REGISTER A MENIAL SOCIAL WORKER

    @PostMapping("/createsocialworker")
    public ApiResponse<String> createSocialWorker(@RequestBody User user) {
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.registerNewUserAccount(user));
        u.setStatus(HttpStatus.CREATED);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }




    // SEARCH FOR USER BY EMAIL FROM MAIN TABLE

    @PostMapping("/searchuser/{search}/search")
    public ApiResponse<List<User>> searchUserByKeyword(@PathVariable("search") String user) {
        logger.info("SEARCH === " + user);
        ApiResponse<List<User>> u = new ApiResponse<>();
        u.setResponse(userinterface.searchForUserUsingAnyKeyword(user));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }




    // SEARCH FOR USER ATTENDANCE

    @PostMapping("/searchuserattendance")
    public ApiResponse<UserClockTime> searchUserAttendanceByEmail(@RequestBody UserClockTime user) {
        ApiResponse<UserClockTime> u = new ApiResponse<>();
        u.setResponse(userinterface.searchUserByEmailForAttendance(user));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }

    // DOWNLOAD EACH USER ATTENDANCE USING EMAIL
    @RequestMapping(value = "/downloadeachattendanceinpdf/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> userAttendance(@PathVariable("email") String email) {

        List<UserClockTime> downloadUser = userinterface.downloadUserAttendanceByEmail(email);

        ByteArrayInputStream bis = pdfreport.userReport(downloadUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename= ATTENDANCE.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    
    // DOWNLOAD EACH USER ATTENDANCE IN CSV
//    @PostMapping("/downloadEachUserAttendanceInCsv")
//    public String downloadEachUserAttendanceInCSV(@RequestBody UserClockTime user, HttpServletResponse response) throws IOException {
//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; file=Attendance.csv");
//        List<UserClockTime> users = userinterface.downloadUserAttendanceByEmail(user);
//        downloadservice.writeObjectToCSV(response.getWriter(), users);
//        return "File successfully downloaded";
//    }


    // GET LOCATION DETAILS SUCH AS LONGITUDE AND LATITUDE
    @PostMapping("/getMedicalCeter")
    public ApiResponse<Locations> getMedicalCenter(@RequestBody Locations location){
     ApiResponse<Locations> u = new ApiResponse<>();
     u.setResponse(userinterface.getLocationDetails(location.getMedicalcenter()));
     u.setStatus(HttpStatus.OK);
     u.setMessage("Success");
        u.setResponsecode("00");
     return u;
    }
    
    
//    // MAP USER TO LOCATION
    @PostMapping("/mapuserlocation")
    public ApiResponse<MapUserLocation> mapUserToLocation(@RequestBody MapUserLocation mapUser){
         ApiResponse<MapUserLocation> u = new ApiResponse<>();
         u.setResponse(userinterface.mapUserToLocation(mapUser.getEmail(), mapUser.getMedicalcenter()));
         u.setStatus(HttpStatus.OK);
         u.setMessage("Success");
        u.setResponsecode("00");
         return u;
    }

    
    // GET USER DESIGNATED LOCATIONS
    @PostMapping("/getuserlocations")
    public ApiResponse<List<MapUserLocation>> getUserLocation(@RequestBody MapUserLocation getLocation){
     ApiResponse<List<MapUserLocation>> u = new ApiResponse<>();
     u.setResponse(userinterface.getUserLocations(getLocation.getEmail()));
     u.setStatus(HttpStatus.OK);
     u.setMessage("Success");
        u.setResponsecode("00");
     return u;
    }


    // UPDATE USER LOCATION
    @PutMapping("/updateuserlocation/{id}")
    public ApiResponse<String> updateUserLocation(@RequestBody MapUserLocation updateUser,@PathVariable("id") long id){
      ApiResponse<String> u = new ApiResponse<>();
      u.setResponse(userinterface.updateUserLocation(updateUser.getMedicalcenter(), id));
      u.setStatus(HttpStatus.OK);
      u.setMessage("Success");
        u.setResponsecode("00");
      return u;
    }


    // PUSH NOTIFICATION MESSAGE
    @PostMapping("/pushnotification")
    public ApiResponse<String> pushNotificationMethod(@RequestBody ObjectNode getUser){
        String medicalcenter = getUser.get("medicalcenter").asText();
        String subject = getUser.get("subject").asText();
        String content = getUser.get("content").asText();
        ApiResponse<String> u = new ApiResponse<>();
        u.setResponse(userinterface.getEmailsForPushNotification(medicalcenter,subject,
                content));
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }



  // API ENDPOINT TO GET TOTAL AMOUNT OF USERS ON THIS APPLICATION
    @GetMapping("/getTotalUsers")
    public ApiResponse<Integer> getTotalUsersOnPlatform(){
        ApiResponse<Integer> u = new ApiResponse<>();
        u.setResponse(userinterface.getTotalAmountOfRegisteredUsersOnThisApplication());
        u.setStatus(HttpStatus.OK);
        u.setMessage("Success");
        u.setResponsecode("00");
        return u;
    }





}
