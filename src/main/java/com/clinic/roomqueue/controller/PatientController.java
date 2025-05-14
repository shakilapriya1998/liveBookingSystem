package com.clinic.roomqueue.controller;

import com.clinic.roomqueue.model.Patient;
import com.clinic.roomqueue.model.PatientStatus;
import com.clinic.roomqueue.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // Show list of patients
    @GetMapping
    public String viewPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        return "patientlist";
    }

    // Show new patient form
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patientform";
    }

    // Save new patient
    @PostMapping
    public String save(@ModelAttribute @Valid Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            return "patientform";
        }

        // Set status and check-in time
        patient.setStatus(PatientStatus.QUEUED);
        patient.setCheckInTime(LocalDateTime.now());

        // Temporarily save without token to get the ID
        patientRepository.save(patient);

        // Assign readable token using ID
        String token = "T" + String.format("%03d", patient.getId());
        patient.setTokenNumber(token);
        patientRepository.save(patient); // Update with token

        return "redirect:/bookings/new";
    }

    // List all patients
    @GetMapping("/all")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        return "patientlist";
    }

    // Show patient queue (status = QUEUED)
    @GetMapping("/queue")
    public String viewQueue(Model model) {
        List<Patient> queue = patientRepository.findByStatusOrderByCheckInTimeAsc(PatientStatus.QUEUED);
        model.addAttribute("queue", queue);
        return "queue";
    }

    // Start consultation (status = IN_CONSULTATION)
    @PostMapping("/{id}/start")
    public String startConsultation(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        if (patient.getStatus().equals(PatientStatus.QUEUED)) {
            patient.setStatus(PatientStatus.IN_CONSULTATION);
            patientRepository.save(patient);
        }
        return "redirect:/patients/queue";
    }

    // Finish consultation (status = DONE)
    @PostMapping("/{id}/done")
    public String finishConsultation(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        if (patient.getStatus().equals(PatientStatus.IN_CONSULTATION)) {
            patient.setStatus(PatientStatus.DONE);
            patientRepository.save(patient);
        }
        return "redirect:/patients/queue";
    }
}
