package com.clinic.roomqueue.service;

import com.clinic.roomqueue.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> findAll();
    Patient findById(Long id);
}
