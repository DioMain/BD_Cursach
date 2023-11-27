package com.medkit.repository;

import com.medkit.model.Symptom;
import com.medkit.model.User;
import com.medkit.model.UserRole;
import com.medkit.repository.interfaces.OracleRepositoryBase;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SymptomRepository extends OracleRepositoryBase<Symptom> {
    public SymptomRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Symptom getById(int id) throws SQLException {
        return get(new Symptom(id, "", ""));
    }

    @Override
    public void insert(Symptom element) throws SQLException {
        String sql = "{call ADMIN.INSERT_NEW_SYMPTOM(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());

            statement.execute();
        }
    }

    @Override
    public void delete(Symptom element) throws SQLException {
        String sql = "{call ADMIN.DELETE_SYMPTOM(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Symptom element) throws SQLException {
        String sql = "{call ADMIN.UPDATE_SYMPTOM(?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());

            statement.execute();
        }
    }

    @Override
    public Symptom get(Symptom element) throws SQLException {
        List<Symptom> symptoms;

        String sql = "{call ? := ADMIN.GET_SYMPTOM(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            symptoms = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return symptoms.get(0);
    }

    @Override
    public List<Symptom> getAll() throws SQLException {
        List<Symptom> symptoms;

        String sql = "{call ? := ADMIN.GET_ALL_SYMPTOMS()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            symptoms = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return symptoms;
    }

    @Override
    protected List<Symptom> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Symptom> symptoms = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("user_id");

            String name = resultSet.getString("name");
            String description = resultSet.getString("description");

            symptoms.add(new Symptom(id, name, description));
        }

        return symptoms;
    }
}
