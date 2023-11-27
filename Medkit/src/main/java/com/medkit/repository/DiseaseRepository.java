package com.medkit.repository;

import com.medkit.model.Disease;
import com.medkit.model.Symptom;
import com.medkit.repository.interfaces.OracleRepositoryBase;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiseaseRepository extends OracleRepositoryBase<Disease> {
    public DiseaseRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Disease getById(int id) throws SQLException {
        return get(new Disease(id, "", ""));
    }

    @Override
    public void insert(Disease element) throws SQLException {
        String sql = "{call ADMIN.INSERT_NEW_DISEASE(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());

            statement.execute();
        }
    }

    @Override
    public void delete(Disease element) throws SQLException {
        String sql = "{call ADMIN.DELETE_DISEASE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Disease element) throws SQLException {
        String sql = "{call ADMIN.UPDATE_DISEASE(?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());

            statement.execute();
        }
    }

    @Override
    public Disease get(Disease element) throws SQLException {
        List<Disease> diseases;

        String sql = "{call ? := ADMIN.GET_DISEASE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            diseases = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diseases.get(0);
    }

    @Override
    public List<Disease> getAll() throws SQLException {
        List<Disease> diseases;

        String sql = "{call ? := ADMIN.GET_ALL_DISEASES()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            diseases = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diseases;
    }

    @Override
    protected List<Disease> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Disease> diseases = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("user_id");

            String name = resultSet.getString("name");
            String description = resultSet.getString("description");

            diseases.add(new Disease(id, name, description));
        }

        return diseases;
    }
}
