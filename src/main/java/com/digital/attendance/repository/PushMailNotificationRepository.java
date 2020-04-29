package com.digital.attendance.repository;

import com.digital.attendance.model.PushMailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushMailNotificationRepository extends JpaRepository<PushMailNotification, Long> {

    List<PushMailNotification> findByMedicalcenter(String medicalcenter);
}
