package com.medkit.repository;

import com.medkit.model.User;
import com.medkit.model.UserRole;
import com.medkit.repository.interfaces.OracleRepositoryBase;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends OracleRepositoryBase<User> {
    public UserRepository(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(User element) throws SQLException {

    }

    @Override
    public void delete(User element) throws SQLException {

    }

    @Override
    public void update(User element) throws SQLException {

    }

    @Override
    public User get(User element) throws SQLException {
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.GET_ALL_USERS()}"; // Пример вызова функции с курсором
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR); // Регистрация выходного параметра курсора

            statement.execute();

            users = convertResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public User login(String email, String password) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.GET_CURRENT_USER()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(0, email);
            statement.setString(1, password);

            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            users = convertResultSet(statement.getObject(1, ResultSet.class));
        }

        return users.get(0);
    }

    @Override
    protected List<User> convertResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

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

        return users;
    }
}