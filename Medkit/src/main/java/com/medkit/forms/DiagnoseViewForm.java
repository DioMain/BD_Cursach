package com.medkit.forms;

import com.medkit.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseViewForm {
    private int id;

    private String openDate;
    private String closeDate;

    private String note;
    private String description;

    private Disease disease;

    private User patient;
    private User doctor;

    private int state;

    private List<Symptom> symptoms;
    private List<Medicine> medicines;
}
