/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author longbridge
 */
@Entity
@Table(name = "tbl_maplocation")
@EntityListeners(AuditingEntityListener.class)
public class MapUserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "medicalcenter", nullable = true)
    private String medicalcenter;

    @Column(name = "latitude", nullable = true)
    private String latitude;

    @Column(name = "longitude", nullable = true)
    private String longitude;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalcenter() {
        return medicalcenter;
    }

    public void setMedicalcenter(String medicalcenter) {
        this.medicalcenter = medicalcenter;
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
    
    
    
    

}
