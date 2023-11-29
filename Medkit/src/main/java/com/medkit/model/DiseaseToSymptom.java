package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseToSymptom {
    private int id;

    private int diseaseId;
    private int SymptomId;
}
