package com.medkit.exception;

import com.medkit.model.UserRole;
import lombok.Getter;

public class SessionException extends Exception {
    public SessionException(String message) {
        super(message);
    }
}
