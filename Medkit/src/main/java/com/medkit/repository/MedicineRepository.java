package com.medkit.repository;

import com.medkit.model.Medicine;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MedicineRepository extends OracleRepositoryBase<Medicine> {
    public MedicineRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Medicine getById(int id) throws SQLException {
        return null;
    }

    @Override
    public void insert(Medicine element) throws SQLException {

    }

    @Override
    public void delete(Medicine element) throws SQLException {

    }

    @Override
    public void update(Medicine element) throws SQLException {

    }

    @Override
    public Medicine get(Medicine element) throws SQLException {
        return null;
    }

    @Override
    public List<Medicine> getAll() throws SQLException {
        return null;
    }

    @Override
    protected List<Medicine> parseResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }
}
