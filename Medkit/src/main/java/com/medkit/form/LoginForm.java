package com.medkit.form;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginForm {
    private String email;
    private String password;
}
