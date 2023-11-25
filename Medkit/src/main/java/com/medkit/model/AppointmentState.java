package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentState {
    NOT_CONSISTENT(0), CONSISTENT(1), VISITED(2),
    REJECTED_PATIENT(-1), REJECTED_DOCTOR(-2);

    private final int value;

    public static AppointmentState getByValue (int value) {
        return switch (value) {
            case -2 -> REJECTED_DOCTOR;
            case -1 -> REJECTED_PATIENT;
            case 1 -> CONSISTENT;
            case 2 -> VISITED;
            default -> NOT_CONSISTENT;
        };
    }
}
