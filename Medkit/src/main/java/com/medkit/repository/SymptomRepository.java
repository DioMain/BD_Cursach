package com.medkit.repository;

import com.medkit.model.Symptom;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SymptomRepository extends OracleRepositoryBase<Symptom> {
    public SymptomRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Symptom getById(int id) throws SQLException {
        return null;
    }

    @Override
    public void insert(Symptom element) throws SQLException {

    }

    @Override
    public void delete(Symptom element) throws SQLException {

    }

    @Override
    public void update(Symptom element) throws SQLException {

    }

    @Override
    public Symptom get(Symptom element) throws SQLException {
        return null;
    }

    @Override
    public List<Symptom> getAll() throws SQLException {
        return null;
    }

    @Override
    protected List<Symptom> parseResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }
}
