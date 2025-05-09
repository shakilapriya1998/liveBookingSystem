package com.clinic.roomqueue.controller;

import com.clinic.roomqueue.dto.BookingFormDTO;
import com.clinic.roomqueue.model.Booking;
import com.clinic.roomqueue.model.Doctor;
import com.clinic.roomqueue.model.Patient;
import com.clinic.roomqueue.service.BookingService;
import com.clinic.roomqueue.service.DoctorService;
import com.clinic.roomqueue.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public BookingController(BookingService bookingService, PatientService patientService, DoctorService doctorService) {
        this.bookingService = bookingService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("bookingForm", new BookingFormDTO());
        model.addAttribute("patients", patientService.findAll());  // Using PatientService
        model.addAttribute("doctors", doctorService.findAll());  // Using DoctorService
        return "booking-form";
    }

    @PostMapping
    public String createBooking(@ModelAttribute BookingFormDTO bookingForm) {
        bookingService.createBooking(bookingForm);
        return "redirect:/bookings/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

    @PostMapping("/booking/complete/{id}")
    public String completeBooking(@PathVariable Long id) {
        bookingService.completeBooking(id);
        return "redirect:/booking/list";
    }

    @GetMapping
    public String listBooking(Model model) {
        model.addAttribute("bookings", bookingService.findAllBookings());  // Fetching bookings from BookingService
        return "booking-list";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id);
        List<Patient> patients = patientService.findAll();
        List<Doctor> doctors = doctorService.findAll();

        model.addAttribute("booking", booking);
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);

        return "booking-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateBooking(@PathVariable Long id, @ModelAttribute Booking booking) {
        booking.setId(id);
        bookingService.updateBooking(booking);
        return "redirect:/bookings";
    }

    @GetMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings";
    }
}
