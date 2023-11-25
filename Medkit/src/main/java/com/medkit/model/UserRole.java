package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("ADMIN"), DOCTOR("DOCTOR"),
    PATIENT("PATIENT"), LOGIN_REGISTRATION("LOGIN_REGISTRATION");

    private final String value;

    public static UserRole getByValue(String value) {
        return switch (value) {
            case "ADMIN" -> UserRole.ADMIN;
            case "DOCTOR" -> UserRole.DOCTOR;
            case "PATIENT" -> UserRole.PATIENT;
            default -> UserRole.LOGIN_REGISTRATION;
        };
    }
}
