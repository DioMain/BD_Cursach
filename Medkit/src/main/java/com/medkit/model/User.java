package com.medkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    private UserRole userRole;
    private String name;
    private String surname;
    private String password;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private Date birthday;
}
