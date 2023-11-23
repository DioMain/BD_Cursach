package com.medkit.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEditorForm {
    @NotEmpty
    String name;
    @NotEmpty
    String surname;
    @NotEmpty
    String patronymic;

    @NotEmpty
    @NotNull
    String birthday;

    @Pattern(regexp = "^\\+\\d{12}$", message = "Не верный формат телефона!")
    String phoneNumber;
}
