package com.medkit.forms;

import com.medkit.model.Disease;
import com.medkit.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseOutEditorForm {
    private int id;

    private String openDate;
    private String closeDate;

    @Length(max = 128)
    private String note;
    @Length(max = 512)
    private String description;

    private int diseaseId;

    @NotNull
    private int patientId;

    private int doctorId;

    private int state;

    private String symptomsJSON;
    private String medicinesJSON;
}
