package com.clinic.roomqueue.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookingFormDTO {
    private Long patientId;
    private Long doctorId;
}
