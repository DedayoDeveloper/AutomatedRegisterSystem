/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.model;

import java.util.List;

/**
 *
 * @author oreoluwa
 */
public class AuthenticationResponse {
    
    private final String jwt;
    private final List<MapUserLocation> locations;
    private final User userDetails;

    public AuthenticationResponse(String jwt, List<MapUserLocation> locations,User userDetails) {
        this.jwt = jwt;
        this.locations = locations;
        this.userDetails = userDetails;
    }



    public User getUserDetails() {
        return userDetails;
    }

    public String getJwt() {

        return jwt;
    }

    public List<MapUserLocation> getLocations() {
        return locations;
    }
}
