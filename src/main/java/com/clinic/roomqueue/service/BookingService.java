package com.clinic.roomqueue.service;

import com.clinic.roomqueue.dto.BookingFormDTO;
import com.clinic.roomqueue.model.*;
import com.clinic.roomqueue.repository.BookingRepository;
import com.clinic.roomqueue.repository.DoctorRepository;
import com.clinic.roomqueue.repository.PatientRepository;
import com.clinic.roomqueue.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private RoomRepository roomRepository;

    public void createBooking(BookingFormDTO form) {
        Patient patient = patientRepository.findById(form.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(form.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Booking booking = new Booking();
        booking.setPatient(patient);
        booking.setDoctor(doctor);
        booking.setBookingTime(LocalDateTime.now());

        // Find available room, if available assign, otherwise, set to QUEUED
        Optional<Room> roomOpt = roomRepository.findAvailableRoom();
        if (roomOpt.isPresent()) {
            booking.setRoom(roomOpt.get());
            booking.setStatus(BookingStatus.ONGOING);
        } else {
            booking.setStatus(BookingStatus.QUEUED);
        }

        bookingRepository.save(booking);
    }

    public void completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Id Not available"));
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        // Find the next queued booking and set it to ongoing
        List<Booking> nextBooking = bookingRepository.findNextQueuedBooking(booking.getDoctor().getId());
        if (!nextBooking.isEmpty()) {
            Booking next = nextBooking.get(0);
            next.setStatus(BookingStatus.ONGOING);
            next.setRoom(booking.getRoom());
            bookingRepository.save(next);
        }
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not Available: " + id));
    }

    public void updateBooking(Booking booking) {
        Booking exist = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new RuntimeException("Id Not available"));

        exist.setDoctor(booking.getDoctor());
        exist.setPatient(booking.getPatient());
        exist.setRoom(booking.getRoom());
        exist.setBookingTime(booking.getBookingTime());
        exist.setStatus(booking.getStatus());
        bookingRepository.save(exist);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id Not available"));

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll(); // Return all bookings
    }
}
