package com.medkit.repository;

import com.medkit.model.Appointment;
import com.medkit.model.AppointmentState;
import com.medkit.model.Medicine;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicineRepository extends OracleRepositoryBase<Medicine> {
    public MedicineRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Medicine getById(int id) throws SQLException {
        return get(new Medicine(id, "", "", "", 0, new Date()));
    }

    @Override
    public void insert(Medicine element) throws SQLException {
        String sql = "{call ADMIN.INSERT_NEW_MEDICINE(?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, element.getName());
            statement.setString(2, element.getDescription());
            statement.setString(3, element.getManufacturer());

            statement.setFloat(4, element.getPrice());

            statement.setDate(5, new java.sql.Date(element.getStartDate().getTime()));

            statement.execute();
        }
    }

    @Override
    public void delete(Medicine element) throws SQLException {
        String sql = "{call ADMIN.DELETE_MEDICINE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Medicine element) throws SQLException {
        String sql = "{call ADMIN.UPDATE_MEDICINE(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.setString(2, element.getName());
            statement.setString(3, element.getDescription());
            statement.setString(4, element.getManufacturer());

            statement.setFloat(5, element.getPrice());

            statement.setDate(6, new java.sql.Date(element.getStartDate().getTime()));

            statement.execute();
        }
    }

    @Override
    public Medicine get(Medicine element) throws SQLException {
        List<Medicine> medicines;

        String sql = "{call ? := ADMIN.GET_MEDICINE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            medicines = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return medicines.get(0);
    }

    @Override
    public List<Medicine> getAll() throws SQLException {
        List<Medicine> medicines;

        String sql = "{call ? := ADMIN.GET_ALL_MEDICINES()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            medicines = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return medicines;
    }

    @Override
    protected List<Medicine> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("medicine_id");

            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String manufacturer = resultSet.getString("manufacturer");

            float price = resultSet.getFloat("price");

            java.sql.Date start_date = resultSet.getDate("start_date");

            medicines.add(new Medicine(id, name, description, manufacturer, price, start_date));
        }

        return medicines;
    }
}
