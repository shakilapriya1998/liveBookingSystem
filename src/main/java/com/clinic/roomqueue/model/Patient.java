package com.clinic.roomqueue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank
    private String email;

    @NotBlank
    private String gender;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime checkInTime;



    public String getFormattedCheckInTime() {
        if (checkInTime == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return checkInTime.format(formatter);
    }


    @Enumerated(EnumType.STRING)
    private PatientStatus status = PatientStatus.QUEUED;

    @Column(unique = true)
    private String tokenNumber;
}
