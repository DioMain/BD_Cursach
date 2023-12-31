package com.medkit.forms;

import com.medkit.model.DiseaseToSymptom;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseEditorForm {
    private int id;

    @NotEmpty(message = "Поле названия пустое!")
    private String name;
    @NotEmpty(message = "Поле описания пустое!")
    private String description;

    private String symptomsJson;
}
