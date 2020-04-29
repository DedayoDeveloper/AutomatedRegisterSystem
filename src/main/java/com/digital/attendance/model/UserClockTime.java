/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author oreoluwa
 */
@Entity
@Table(name = "tbl_timeregister")
@EntityListeners(AuditingEntityListener.class)
public class UserClockTime {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; 
    
    @Column(name = "timein", nullable = true)
    private String timein;

    @Column(name = "timeout", nullable = true)
    private String timeout;
    
    
    @Column(name = "location", nullable = false)
    private String location;

    @JsonIgnore
    @Column(name = "latitude", nullable = false)
    private String latitude;

    @JsonIgnore
     @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "timespent", nullable = true)
    private String timespent;
    
    @Column(name = "date", nullable = false)
    private String date;
    
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "systemlogout", nullable = true)
    private String systemlogout;

    @Column(name = "profession", length = 20, nullable = true)
    private String profession;

    @Column(name = "firstname", length = 50, nullable = true)
    private String firstname;

    @Column(name = "lastname", length = 50, nullable = true)
    private String lastname;

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSystemlogout() {
        return systemlogout;
    }

    public void setSystemlogout(String systemlogout) {
        this.systemlogout = systemlogout;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    
    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimein() {
        return timein;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }


    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }
    
    
    
}
