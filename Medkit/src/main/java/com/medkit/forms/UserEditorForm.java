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
    @NotEmpty(message = "Поле имени пустое!")
    String name;
    @NotEmpty(message = "Поле фамилии пустое!")
    String surname;
    @NotEmpty(message = "Поле отчетсва пустое!")
    String patronymic;

    @NotEmpty
    @NotNull(message = "Поле даты рождения пустое!")
    String birthday;

    @Pattern(regexp = "^\\+\\d{12}$", message = "Не верный формат телефона!")
    String phoneNumber;
}
