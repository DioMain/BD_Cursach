package com.medkit.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentForm {
    @NotNull
    private int doctorId;
    @NotNull
    private int patientId;

    @NotNull
    @NotEmpty
    private String appointmentDate;

    @NotNull
    private int state;
}
