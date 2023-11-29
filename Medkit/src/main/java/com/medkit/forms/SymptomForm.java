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

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
