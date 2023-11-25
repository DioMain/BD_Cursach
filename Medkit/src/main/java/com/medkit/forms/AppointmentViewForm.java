package com.medkit.forms;

import com.medkit.model.Appointment;
import com.medkit.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewForm {
    Appointment appointment;

    User patient;
    User doctor;
}
