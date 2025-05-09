package com.clinic.roomqueue.repository;

import com.clinic.roomqueue.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.doctor.id = :doctorId AND b.status = 'QUEUED' ORDER BY b.bookingTime ASC")
    List<Booking> findNextQueuedBooking(Long doctorId);
}
