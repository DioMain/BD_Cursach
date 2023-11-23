package com.medkit.forms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginForm {
    private String email;
    private String password;
}
