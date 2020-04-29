/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.repository;

import com.digital.attendance.model.SupervisorAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author longbridge
 */
@Repository
public interface SupervisorAdminRepository extends JpaRepository<SupervisorAdmin, Long>{
    
    SupervisorAdmin findByEmail(String email);

    @Query("select u from SupervisorAdmin u order by u.id desc ")
    List<SupervisorAdmin> getListOfAllSupervisors();
    
}
