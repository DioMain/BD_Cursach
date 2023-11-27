package com.medkit.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineForm {
    private int id;

    @NotEmpty
    @Length(max = 64)
    private String name;
    @Length(max = 512)
    private String description;
    @Length(max = 64)
    private String manufacturer;

    @NotNull
    private float price;

    @NotEmpty
    @NotNull
    private String startDate;
}
