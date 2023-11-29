package com.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Program {
    public static void main(String[] args) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File("D:\\UnicLabs\\3Cource\\BD_Cursach\\EXPORT.json");

        List<User> users = mapper.readValue(file, new TypeReference<List<User>>() {});

        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//DESKTOP-SE50MB3:1521/MEDKIT_PDB", "sys as sysdba", "1234");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        users.forEach(i -> {
            String sql = "{call ADMIN.REGISTRATION_NEW_USER(?, ?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.setString(1, i.USER_ROLE);
                statement.setString(2, i.NAME);
                statement.setString(3, i.SURNAME);
                statement.setString(4, i.PATRONYMIC);
                statement.setString(5, i.PASSWORD);
                statement.setDate(6, new java.sql.Date(simpleDateFormat.parse(i.BIRTHDAY).getTime()));
                statement.setString(7, i.PHONE_NUMBER);
                statement.setString(8, i.EMAIL);

                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
