package com.medkit.repository;

import com.medkit.model.Appointment;
import com.medkit.model.AppointmentState;
import com.medkit.repository.interfaces.OracleRepositoryBase;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository extends OracleRepositoryBase<Appointment> {

    public AppointmentRepository(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Appointment element) throws SQLException {
        String sql = "{call ADMIN.INSERT_NEW_APPOINTMENT(?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getPatientId());
            statement.setInt(2, element.getDoctorId());
            statement.setDate(3, new java.sql.Date(element.getAppointmentDate().getTime()));

            statement.execute();
        }
    }

    @Override
    public void delete(Appointment element) throws SQLException {
        String sql = "{call ADMIN.DELETE_APPOINTMENT(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getPatientId());

            statement.execute();
        }
    }

    @Override
    public void update(Appointment element) throws SQLException {
        String sql = "{call ADMIN.UPDATE_APPOINTMENT(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setInt(2, element.getState().getValue());

            statement.execute();
        }
    }

    @Override
    public Appointment get(Appointment element) throws SQLException {
        List<Appointment> appointments;

        String sql = "{call ? := ADMIN.GET_APPOINTMENT(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            appointments = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return appointments.get(0);
    }

    @Override
    public Appointment getById(int id) throws SQLException {
        List<Appointment> appointments;

        String sql = "{call ? := ADMIN.GET_APPOINTMENT(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, id);

            statement.execute();

            appointments = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return appointments.get(0);
    }

    public List<Appointment> getByUserId(int userId) throws SQLException {
        List<Appointment> appointments;

        String sql = "{call ? := ADMIN.GET_APPOINTMENTS_BY_USERID(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, userId);

            statement.execute();

            appointments = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return appointments;
    }

    @Override
    public List<Appointment> getAll() throws SQLException {
        List<Appointment> appointments;

        String sql = "{call ? := ADMIN.GET_ALL_APPOINTMENTS()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            appointments = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return appointments;
    }

    @Override
    protected List<Appointment> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("appointment_id");

            int patient_id = resultSet.getInt("patient_id");
            int doctor_id = resultSet.getInt("doctor_id");

            Date appointment_date = resultSet.getDate("appointment_date");

            AppointmentState state = AppointmentState.getByValue(resultSet.getInt("appointment_state"));

            appointments.add(new Appointment(id, patient_id, doctor_id, appointment_date, state));
        }

        return appointments;
    }
}
