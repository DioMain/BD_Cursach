package com.medkit.forms;

import com.medkit.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseInEditorForm {
    private int id;

    private String openDate;
    private String closeDate;

    private String note;
    private String description;

    private String diseaseJson;

    private String patientJson;
    private String doctorJson;

    private int state;

    private String symptomsJSON;
    private String medicinesJSON;
}
