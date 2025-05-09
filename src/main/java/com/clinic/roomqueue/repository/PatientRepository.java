package com.clinic.roomqueue.repository;

import com.clinic.roomqueue.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
}
