package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnose {
    private int id;

    private int patientId;
    private int doctorId;

    private int diseaseId;

    private Date openDate;
    private Date closeDate;

    private String note;
    private String description;

    private DiagnoseState state;
}
