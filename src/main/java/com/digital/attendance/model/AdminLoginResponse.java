package com.digital.attendance.model;

public class AdminLoginResponse {

    private final String jwt;

    public AdminLoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {

        return jwt;
    }
}
