package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseToSymptom {
    private int id;

    private int diagnoseId;
    private int SymptomId;
}
