package com.medkit.forms;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEditorForm {
    String name;
    String surname;
    String patronymic;

    String birthday;

    @Pattern(regexp = "^\\+\\d{12}$", message = "Не верный формат телефона!")
    String phoneNumber;
}
