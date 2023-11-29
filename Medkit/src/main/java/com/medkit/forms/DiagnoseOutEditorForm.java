package com.medkit.forms;

import com.medkit.model.Disease;
import com.medkit.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseOutEditorForm {
    private int id;

    private String openDate;
    private String closeDate;

    private String note;
    private String description;

    private int diseaseId;

    private int patientId;
    private int doctorId;

    private int state;

    private String symptomsJSON;
    private String medicinesJSON;
}
