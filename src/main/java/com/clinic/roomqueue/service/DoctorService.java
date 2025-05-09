package com.clinic.roomqueue.service;

import com.clinic.roomqueue.model.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    List<Doctor> findAll();
    Doctor findById(Long id);
}
