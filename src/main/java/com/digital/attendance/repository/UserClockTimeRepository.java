/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.repository;

import com.digital.attendance.model.UserClockTime;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author oreoluwa
 */
@Repository
public interface UserClockTimeRepository extends JpaRepository<UserClockTime, Long>{

    UserClockTime findUserByEmail(String email);

    List<UserClockTime> findByLocation(String location);


//    @Query("select t1 from UserClockTime t1 where t1.date = (select max(t1.date) from UserClockTime t1) and t1.email = :email and t1.location = :location")
//    UserClockTime getUserClockInDetails(@Param("email") String email,@Param("location") String location);

//    @Query(nativeQuery = true, value ="select * from tbl_timeregister  order by id desc")
//    List<UserClockTime> getAllListOfClockedUsersByLimit();




    @Transactional
    @Modifying
    @Query("update UserClockTime u set u.timeout = :timeout where u.timeout is NULL ")
    int autoSetClockoutTime(@Param("timeout") String timeout);

    @Transactional
    @Modifying
    @Query("update UserClockTime u set u.systemlogout = :systemlogout where u.timeout is NULL ")
    int autoSetClockOutSystem(@Param("systemlogout") String systemlogout);

    @Transactional
    @Modifying
    @Query("update UserClockTime u set u.timespent = :timespent where u.timeout = '24:00:00' and u.timein = :timein")
    int setAutoSetTimeCount(@Param("timespent") String timespent,@Param("timein") String timein);

    @Query( "SELECT u.timein FROM UserClockTime u WHERE timeout = '24:00:00' ")
    List<String> getListOfAllThoseWhoSystemClockedOut();

//    @Query("SELECT TIMEDIFF('22:30:00', :timein)")
//    String calculateTimeIn(@Param("timein") String timein);



    @Query("select u.timespent from UserClockTime u where email = :email")
    String getTimeCount(@Param("email")String email);
    
    
    @Query("select u from UserClockTime u where u.location = :location")
    List<UserClockTime> downloadUserAttendance(@Param("location") String location);
    
    @Transactional
    @Modifying
    @Query("update UserClockTime u SET u.timespent = HOUR(TIMEDIFF(u.timeout, u.timein)) where u.email = :email and u.timein = :timein and u.date = :date")
    int setTimeCount(@Param("email") String email,@Param("timein") String timein,@Param("date") String date);


    @Query("select count(*) from UserClockTime u where u.date = :date")
    String UserCount (@Param("date") String date);
    
    
    @Query("select u from UserClockTime u where u.email = :email")
    List<UserClockTime> getAllUserHistory(@Param("email") String email);
    
    @Query("select u from UserClockTime u where u.email = :email")
    UserClockTime searchUserByEmailForAttendance(@Param("email") String email);
    
    @Query("select u from UserClockTime u where u.email = :email")
    List<UserClockTime> downloadEachUserAttendanceByEmail(@Param("email") String email);
    
    @Query("select u from UserClockTime u where u.email = :email and u.location = :location and u.timein = :timein")
    UserClockTime getExactUserToClockout(@Param("email") String email, @Param("location") String location, @Param("timein") String timein);


    @Query("select t1.timein,t1.timeout from UserClockTime t1 where t1.date = CURDATE() and t1.email = :email")
    List<String> getUserClockInDetailsForAdmin(@Param("email") String email);

    @Query("select u from UserClockTime u where u.date = CURDATE()")
    List<UserClockTime> getListOfClockedInUsersForToday();

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Query(nativeQuery = true, value = "select * from tbl_timeregister where date between :startdate and :enddate ")
    List<UserClockTime> filterSearchOne(@Param("startdate") String startdate, @Param("enddate") String enddate);

    @Query("select u from UserClockTime  u where u.location = :location and u.date between :startdate and :enddate")
    List<UserClockTime> filterSearchTwo(@Param("location") String location, @Param("startdate") String startdate, @Param("enddate") String enddate);

    @Query("select u from UserClockTime u where u.profession = :profession and u.date between :startdate and :enddate")
    List<UserClockTime> filterSearchThree(@Param("profession") String profession, @Param("startdate") String startdate, @Param("enddate") String enddate);

    @Query("select u from UserClockTime u where u.profession = :profession and u.location = :location")
    List<UserClockTime> filterSearchFour(@Param("profession") String profession, @Param("location") String location);

    @Query("select u from UserClockTime u where u.profession = :profession and u.location = :location and u.date between :startdate and :enddate")
    List<UserClockTime> filterSearchFive(@Param("profession") String profession, @Param("location") String location, @Param("startdate") String startdate, @Param("enddate") String enddate);

    @Query("select u from UserClockTime u where u.profession = :profession")
    List<UserClockTime> filterSearchSix(@Param("profession") String profession);

    @Query("select u from UserClockTime  u where u.location = :location")
    List<UserClockTime> filterSearchSeven(@Param("location") String location);

//    ---------------------------------------------------------------------------------------------------------------------------
      @Query(nativeQuery = true, value = "select email , firstname , lastname, concat(SEC_TO_TIME( SUM( TIME_TO_SEC( timeSpent ) ) ), '') AS TotalHours from tbl_timeregister  where date between :startdate and :enddate GROUP BY email,firstname,lastname")
      List<Object> getTotalHoursWorked(@Param("startdate") String startdate, @Param("enddate") String enddate);



}


