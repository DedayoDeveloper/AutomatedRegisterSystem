package com.digital.attendance.repository;

import com.digital.attendance.model.UserTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypes, Long> {

//    UserTypes findByUsertype(String usertype);

    @Query("select u.usertype from UserTypes  u where u.usertype = :usertype")
    String getUserType(@Param("usertype") String usertype);
}
