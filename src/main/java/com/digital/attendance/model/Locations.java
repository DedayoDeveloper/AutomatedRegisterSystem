/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author longbridge
 */
@Entity
@Table(name = "tbl_locations")
@EntityListeners(AuditingEntityListener.class)
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "longitude", nullable = true)
    private String longitude;

    @Column(name = "latitude", nullable = true)
    private String latitude;

    @Column(name = "medicalcenter", nullable = false)
    private String medicalcenter;

    public String getMedicalcenter() {
        return medicalcenter;
    }

    public void setMedicalcenter(String medicalcenter) {
        this.medicalcenter = medicalcenter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
