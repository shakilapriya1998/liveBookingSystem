package com.clinic.roomqueue.repository;

import com.clinic.roomqueue.model.Patient;
import com.clinic.roomqueue.model.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByStatusOrderByCheckInTimeAsc(PatientStatus status);
}

