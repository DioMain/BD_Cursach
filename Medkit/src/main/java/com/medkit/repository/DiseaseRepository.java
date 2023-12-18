package com.medkit.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medkit.forms.IdForm;
import com.medkit.model.Disease;
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
        String sql = "{call ADMIN.DISEASE_PACK.INSERT_NEW_DISEASE(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());

            statement.execute();
        }
    }

    public Disease insertWithReturn(Disease element) throws SQLException {
        String sql = "{call ? := ADMIN.DISEASE_PACK.INSERT_NEW_DISEASE_RET_ID(?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.INTEGER);

            statement.setString(2, element.getName());
            statement.setString(3, element.getDescription());

            statement.execute();

            element.setId(statement.getObject(1, Integer.class));
        }

        return element;
    }

    @Override
    public void delete(Disease element) throws SQLException {
        String sql = "{call ADMIN.DISEASE_PACK.DELETE_DISEASE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Disease element) throws SQLException {
        String sql = "{call ADMIN.DISEASE_PACK.UPDATE_DISEASE(?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(2, element.getName());
            statement.setString(3, element.getDescription());

            statement.execute();
        }
    }

    @Override
    public Disease get(Disease element) throws SQLException {
        List<Disease> diseases;

        String sql = "{call ? := ADMIN.DISEASE_PACK.GET_DISEASE(?)}";
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

        String sql = "{call ? := ADMIN.DISEASE_PACK.GET_ALL_DISEASES()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            diseases = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diseases;
    }

    public List<Disease> analyze(List<IdForm> forms) throws SQLException, JsonProcessingException {
        List<Disease> diseases;

        String sql = "{call ? := ADMIN.DIAGNOSE_PACK.ANALYSE_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(forms);

            statement.setString(2, json);

            statement.execute();

            diseases = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diseases;
    }

    @Override
    protected List<Disease> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Disease> diseases = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("disease_id");

            String name = resultSet.getString("name");
            String description = resultSet.getString("description");

            diseases.add(new Disease(id, name, description));
        }

        return diseases;
    }
}
