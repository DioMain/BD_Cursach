package com.importer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.Encoder;
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

        File file = new File("D:\\UnicLabs\\3Cource\\BD_Cursach\\USERS.json");

        List<User> users = mapper.readValue(file, new TypeReference<List<User>>() {});

        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//DESKTOP-SE50MB3:1521/MEDKIT_PDB", "sys as sysdba", "1234");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        users.forEach(i -> {
            String sql = "{call ADMIN.REGISTRATION_NEW_USER(?, ?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.setString(1, i.Role);
                statement.setString(2, i.Name);
                statement.setString(3, i.Surname);
                statement.setString(4, i.Patronymic);
                statement.setString(5, i.Password);
                statement.setDate(6, new java.sql.Date(simpleDateFormat.parse(i.Birthday).getTime()));
                statement.setString(7, i.PhoneNumber);
                statement.setString(8, i.Email);

                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
