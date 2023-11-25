package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiagnoseState {
    OPENED(0), CLOSED(1),
    CANCELED(-1);

    private final int value;

    public static DiagnoseState getByValue (int value) {
        return switch (value) {
            case -1 -> CANCELED;
            case 1 -> CLOSED;
            default -> OPENED;
        };
    }
}
