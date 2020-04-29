/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.model;

import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


/**
 *
 * @author oreoluwa
 */
@Entity
@Table(name = "tbl_employees")
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


  public User(){

  }


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
  
    
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "accountnumber", nullable = false)
    private String accountnumber;
    
    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    
     @Column(name = "location", nullable = false)
    private String location;
    
     @Column(name = "enabled", nullable = false)
     private int enabled;
     
     @Column(name = "password", nullable = false)
     private String password;
     
     @Column(name = "identification", nullable = false)
     private String identification;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

      @Column(name = "usertype", nullable = false)
     private String usertype;
      
       @Column(name = "bankname", nullable = false)
     private String bankname;
       
        @Column(name = "gender", nullable = false)
     private String gender;
        
     @Column(name = "dateofbirth", nullable = false)
     private String dateofbirth;
     
      @Column(name = "emailtoken", nullable = false)
     private String emailtoken;
      
      @Column(name = "updatepasswordtoken", nullable = true)
     private String updatepasswordtoken;
      
     @Column(name = "bvnnumber", nullable = false)
     private String bvnnumber;

     @Column(name = "dateregistered", nullable = true)
     @Temporal(TemporalType.DATE)
     private Date dateregistered;

     @Column(name = "userrole", nullable = true)
     private String userrole;

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    

  
    public String getBvnnumber() {
        return bvnnumber;
    }

    public void setBvnnumber(String bvnnumber) {
        this.bvnnumber = bvnnumber;
    }
     
      
    public String getUpdatepasswordtoken() {
        return updatepasswordtoken;
    }

    public void setUpdatepasswordtoken(String updatepasswordtoken) {
        this.updatepasswordtoken = updatepasswordtoken;
    }
      

    public String getEmailtoken() {
        return emailtoken;
    }

    public void setEmailtoken(String emailtoken) {
        this.emailtoken = emailtoken;
    }
     
     
    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

 

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
     
     
     
      
      

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

  

   
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
     
      
      

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(Date dateregistered) {
        this.dateregistered = dateregistered;
    }
}
