package com.clinic.roomqueue.repository;

import com.clinic.roomqueue.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT b.room.id FROM Booking b WHERE b.status = 'ONGOING')")
    Optional<Room> findAvailableRoom(); //  No parameter

}
