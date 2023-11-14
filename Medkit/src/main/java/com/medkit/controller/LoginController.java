package com.medkit.controller;

import com.medkit.model.User;

import com.medkit.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class LoginController {
    @GetMapping(value = {"/login", "/index", "/"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("Login");

        String jdbcUrl = "jdbc:oracle:thin:@//DESKTOP-SE50MB3:1521/MEDKIT_PDB";
        String username = "sys as sysdba";
        String password = "1234";

        List<User> users = new ArrayList<>();
        String errorMessage = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sql = "{call ? := ADMIN.GET_ALL_USERS()}"; // Пример вызова функции с курсором
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.registerOutParameter(1, OracleTypes.CURSOR); // Регистрация выходного параметра курсора

                statement.execute();

                try (ResultSet resultSet = (ResultSet) statement.getObject(1)) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("user_id");

                        UserRole role = UserRole.getRoleByValue(resultSet.getString("user_role"));

                        String name = resultSet.getString("name");
                        String surname = resultSet.getString("surname");
                        String patronymic = resultSet.getString("patronymic");

                        Date birthday = resultSet.getDate("birthday");

                        String phoneNumber = resultSet.getString("phone_number");
                        String email = resultSet.getString("email");

                        users.add(new User(id, role, name, surname, patronymic, phoneNumber, email, birthday));
                    }
                }
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        request.setAttribute("users", users);
        request.setAttribute("error", errorMessage);

        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("sss");
            session = request.getSession(true);
        }

        session.setMaxInactiveInterval(1);

        return mav;
    }
}
