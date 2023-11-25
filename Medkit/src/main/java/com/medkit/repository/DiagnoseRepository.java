package com.medkit.repository;

import com.medkit.model.Diagnose;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DiagnoseRepository extends OracleRepositoryBase<Diagnose> {
    public DiagnoseRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Diagnose getById(int id) throws SQLException {
        return null;
    }

    @Override
    public void insert(Diagnose element) throws SQLException {

    }

    @Override
    public void delete(Diagnose element) throws SQLException {

    }

    @Override
    public void update(Diagnose element) throws SQLException {

    }

    @Override
    public Diagnose get(Diagnose element) throws SQLException {
        return null;
    }

    @Override
    public List<Diagnose> getAll() throws SQLException {
        return null;
    }

    @Override
    protected List<Diagnose> parseResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }
}
