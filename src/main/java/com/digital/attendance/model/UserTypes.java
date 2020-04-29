package com.digital.attendance.model;


import javax.persistence.*;

@Entity
@Table(name = "tbl_usertypes")
public class UserTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="usertype",nullable = false)
    private String usertype;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
