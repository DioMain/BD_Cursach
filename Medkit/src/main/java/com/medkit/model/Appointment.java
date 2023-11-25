package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {
    private int id;

    private int patientId;
    private int doctorId;

    private Date appointmentDate;

    private AppointmentState state;
}
