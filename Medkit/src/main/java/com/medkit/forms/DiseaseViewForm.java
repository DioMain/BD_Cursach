package com.medkit.forms;

import com.medkit.model.DiseaseToSymptom;
import com.medkit.model.Symptom;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseViewForm {
    private int id;

    private String name;
    private String description;

    private List<Symptom> symptoms;
}
