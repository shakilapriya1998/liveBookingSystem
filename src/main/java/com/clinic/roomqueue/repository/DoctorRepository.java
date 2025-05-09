package com.clinic.roomqueue.repository;

import com.clinic.roomqueue.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

}
