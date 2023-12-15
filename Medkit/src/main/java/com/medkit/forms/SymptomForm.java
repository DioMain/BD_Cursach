package com.medkit.forms;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymptomForm {
    private int id;

    @NotEmpty(message = "Поле названия пустое!")
    private String name;

    private String description;
}
