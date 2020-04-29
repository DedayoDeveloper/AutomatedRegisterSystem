/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.repository;

import com.digital.attendance.model.MapUserLocation;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author longbridge
 */
@Repository
public interface MapUserLocationRepository extends JpaRepository<MapUserLocation, Long>{
    
//    @Query("select l from MapUserLocation l where l.email = :email")
//    List<MapUserLocation> getUserLocations(@Param("email") String email);

    List<MapUserLocation> findByEmail(String email);

    List<MapUserLocation> findByMedicalcenter(String medicalcenter);


    MapUserLocation findByEmailAndMedicalcenter(String email, String medicalcenter);
    
    @Query("select m.medicalcenter from MapUserLocation m where m.email = :email and m.medicalcenter = :medicalcenter")
    String confirmClockInCenter(@Param("email") String email,@Param("medicalcenter") String medicalcenter);
    
    @Query("select m.longitude from MapUserLocation m where m.email = :email and m.medicalcenter = :medicalcenter")
    String confirmClockInCenterForLongitude(@Param("email") String email,@Param("medicalcenter") String medicalcenter);
    
    @Query("select m.latitude from MapUserLocation m where m.email = :email and m.medicalcenter = :medicalcenter")
    String confirmClockInCenterForLatitude(@Param("email") String email,@Param("medicalcenter") String medicalcenter);
    
    @Transactional
    @Modifying
    @Query("update MapUserLocation m set m.longitude = :longitude , m.latitude = :latitude , m.medicalcenter = :medicalcenter where m.id = :id")
    int updateUserLocation(@Param("longitude") String longitude, @Param("latitude") String latitude, 
            @Param("medicalcenter") String medicalcenter, @RequestParam long id);


    @Query("select u.email from MapUserLocation u where u.medicalcenter = :medicalcenter")
    List<String> getAllEmailsForPushNotification(@Param("medicalcenter") String medicalcenter);
}
