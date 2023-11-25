package com.medkit.repository;

import com.medkit.model.Disease;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DiseaseRepository extends OracleRepositoryBase<Disease> {
    public DiseaseRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Disease getById(int id) throws SQLException {
        return null;
    }

    @Override
    public void insert(Disease element) throws SQLException {

    }

    @Override
    public void delete(Disease element) throws SQLException {

    }

    @Override
    public void update(Disease element) throws SQLException {

    }

    @Override
    public Disease get(Disease element) throws SQLException {
        return null;
    }

    @Override
    public List<Disease> getAll() throws SQLException {
        return null;
    }

    @Override
    protected List<Disease> parseResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }
}
