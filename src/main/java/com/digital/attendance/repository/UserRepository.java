/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.repository;

import com.digital.attendance.model.User;
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
public interface UserRepository extends JpaRepository<User , Long>{
    
    User findByEmail(String email);



    User getUserByEmail(String email);

    User findById(long id);
    
    void deleteByEmail(String email);

    @Query("select u.email from User u")
    List<String> getListOfAllEmailInApplication();

//    @Query("select u from User u order by id desc")
//    List<User> findAllUsersInDescOrder();
    
    @Query("select u.email from User u where u.emailtoken = :emailtoken")
    String getEmailToken(@Param("emailtoken") String emailtoken);

    
    @Transactional
    @Modifying
    @Query("update User u set u.enabled = 1 where u.email = :email")
    int authenticateUser (@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled = 2 where u.email = :email")
    int deactivateUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled = 1 where u.email = :email")
    int reactivateUser(@Param("email") String email);

    
    @Transactional
    @Modifying
    @Query("update User u set u.updatepasswordtoken = :updatepasswordtoken where u.email = :email")
    int updatePasswordToken (@Param("updatepasswordtoken") String updatepasswordtoken, @Param("email") String email);
    
    
    @Query("select u from User u where u.updatepasswordtoken = :updatepasswordtoken and u.email = :email")
    User confirmPasswordToken(@Param("updatepasswordtoken") String updatepasswordtoken, @Param("email") String email);
    
    
    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.email = :email")
    int passwordReset(@Param("password") String password, @Param("email") String email);
    
    
    @Query("select u from User u where u.email = :email")
    User searchUserByEmail(@Param("email") String email);


    @Query("select u from User u where u.enabled = 0 order by u.dateregistered desc ")
    List<User> getUnauthorizedUsers();

    @Query("select u from User u where u.enabled = 2 order by u.id desc ")
    List<User> getListOfDeactivatedUsers();

    @Query("select count(*) from User u where u.enabled = 2")
    int getCountOfDeactivatedUsers();


    @Query("select count(u) from User u where u.enabled = 0")
    int getUnauthorizedUsersInCount();


    List<User> findByEmailOrFirstnameOrLastnameOrPhonenumberOrLocationOrUsertype(@Param("email") String email,
                                                                       @Param("Firstname") String Firstname,
                                                                       @Param("Lastname") String Lastname,
                                                                       @Param("Phonenumber") String Phonenumber,
                                                                       @Param("Location") String Location,
                                                                                 @Param("usertype") String usertype);


    @Query("select count(u)  from User u")
    int getNumberOfUserOnThePlatform();
    
    
}
