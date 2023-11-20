package com.medkit.exception;

import com.medkit.model.UserRole;

public class UnknownUserRoleException extends Exception{
    public UnknownUserRoleException(UserRole role) {
        super("Unknown user role " + role + "!");
    }
}
