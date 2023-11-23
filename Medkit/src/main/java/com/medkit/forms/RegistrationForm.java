package com.medkit.forms;

import com.medkit.model.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {
    @NotEmpty(message = "Поле имени пустое!")
    private String name;
    @NotEmpty(message = "Поле фамилии пустое!")
    private String surname;
    @NotEmpty(message = "Поле отчества пустое!")
    private String patronymic;

    @NotEmpty(message = "Пароль должен быть указан!")
    private String password;
    @NotEmpty(message = "Подтверждение пароля должено быть указано!")
    private String confirmPassword;

    @NotNull(message = "Роль должна быть выбрана!")
    private String role;

    @Pattern(regexp = "^\\+\\d{12}$", message = "Не верный формат телефона!")
    private String phoneNumber;
    @NotEmpty(message = "Поле почты пустое!")
    private String email;
    @NotEmpty(message = "Дата рождения должна быть указана!")
    @NotNull(message = "Дата рождения должна быть указана!")
    private String birthday;
}
