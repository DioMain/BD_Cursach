package com.medkit.repository;

import com.medkit.model.Appointment;
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
        String sql = "{call ADMIN.USER_PACK.REGISTRATION_NEW_USER(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, element.getUserRole().getValue());
            statement.setString(2, element.getName());
            statement.setString(3, element.getSurname());
            statement.setString(4, element.getPatronymic());
            statement.setString(5, element.getPassword());
            statement.setDate(6, new java.sql.Date(element.getBirthday().getTime()));
            statement.setString(7, element.getPhoneNumber());
            statement.setString(8, element.getEmail());

            statement.execute();
        }
    }

    @Override
    public void delete(User element) throws SQLException {
        String sql = "{call ADMIN.USER_PACK.DELETE_CURRENT_USER(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(User element) throws SQLException {
        String sql = "{call ADMIN.USER_PACK.UPDATE_CURRENT_USER(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(2, element.getName());
            statement.setString(3, element.getSurname());
            statement.setString(4, element.getPatronymic());
            statement.setDate(5, new java.sql.Date(element.getBirthday().getTime()));
            statement.setString(6, element.getPhoneNumber());

            statement.execute();
        }
    }

    @Override
    public User get(User element) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USER(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users.get(0);
    }

    @Override
    public User getById(int id) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USER(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, id);

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users.get(0);
    }


    @Override
    public List<User> getAll() throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_ALL_USERS()}"; // Пример вызова функции с курсором
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR); // Регистрация выходного параметра курсора

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public List<User> getFirst(int count) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_FIRST_USERS(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.setInt(2, count);

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public List<User> getByEmail(String email) throws    SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USER_BY_EMAIL(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.setString(2, email);

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public List<User> getByName(String name, String surname, String patronymic) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USERS_BY_NAME(?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setString(4, patronymic);

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public List<User> getByNameAndRole(String name, String surname, String patronymic, UserRole role) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USERS_BY_NAME_AND_ROLE(?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setString(4, patronymic);
            statement.setString(5, role.getValue());

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    public User login(String email, String password) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_CURRENT_USER(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.setString(2, email);
            statement.setString(3, password);


            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users.get(0);
    }

    public List<User> getByRole(UserRole role) throws SQLException {
        List<User> users;

        String sql = "{call ? := ADMIN.USER_PACK.GET_USERS_BY_ROLE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, role.getValue());

            statement.execute();

            users = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return users;
    }

    @Override
    protected List<User> parseResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("user_id");

            UserRole role = UserRole.getByValue(resultSet.getString("user_role"));

            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String patronymic = resultSet.getString("patronymic");
            String password = resultSet.getString("password");

            Date birthday = resultSet.getDate("birthday");

            String phoneNumber = resultSet.getString("phone_number");
            String email = resultSet.getString("email");

            users.add(new User(id, role, name, surname, password, patronymic, phoneNumber, email, birthday));
        }

        return users;
    }
}