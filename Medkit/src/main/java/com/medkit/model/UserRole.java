package com.medkit.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"), DOCTOR("DOCTOR"),
    PATIENT("PATIENT"), LOGIN_REGISTRATION("LOGIN_REGISTRATION");

    private final String value;

    UserRole(String value){
        this.value = value;
    }

    public static UserRole getRoleByValue(String value) {
        return switch (value) {
            case "ADMIN" -> UserRole.ADMIN;
            case "DOCTOR" -> UserRole.DOCTOR;
            case "PATIENT" -> UserRole.PATIENT;
            default -> UserRole.LOGIN_REGISTRATION;
        };
    }
}
