package com.medkit.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentUpdateForm {
    private int appointmentId;
    private String userRole;
}
