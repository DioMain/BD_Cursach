package com.medkit.repository;

import com.medkit.model.Diagnose;
import com.medkit.model.DiagnoseState;
import com.medkit.repository.interfaces.OracleRepositoryBase;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiagnoseRepository extends OracleRepositoryBase<Diagnose> {
    public DiagnoseRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Diagnose getById(int id) throws SQLException {
        return get(new Diagnose(id, 0, 0, 0, new Date(), new Date(), "", "", DiagnoseState.OPENED));
    }

    @Override
    public void insert(Diagnose element) throws SQLException {
        String sql = "{call ADMIN.DIAGNOSE_PACK.INSERT_NEW_DIAGNOSE(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getDoctorId());
            statement.setInt(2, element.getPatientId());
            statement.setInt(3, element.getDiseaseId());

            statement.setDate(4, new java.sql.Date(element.getOpenDate().getTime()));
            statement.setDate(5, new java.sql.Date(element.getCloseDate().getTime()));

            statement.setString(6, element.getNote());
            statement.setString(7, element.getDescription());

            statement.setInt(8, element.getState().getValue());

            statement.execute();
        }
    }

    public Diagnose insertWithReturn(Diagnose element) throws SQLException {
        String sql = "{call ? := ADMIN.DIAGNOSE_PACK.INSERT_NEW_DIAGNOSE_RET_ID(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.INTEGER);

            statement.setInt(2, element.getDoctorId());
            statement.setInt(3, element.getPatientId());
            statement.setInt(4, element.getDiseaseId());

            statement.setDate(5, new java.sql.Date(element.getOpenDate().getTime()));

            if (element.getCloseDate() != null)
                statement.setDate(6, new java.sql.Date(element.getCloseDate().getTime()));
            else
                statement.setDate(6, null);

            statement.setString(7, element.getNote());
            statement.setString(8, element.getDescription());

            statement.setInt(9, element.getState().getValue());

            statement.execute();

            element.setId(statement.getObject(1, Integer.class));
        }

        return element;
    }

    @Override
    public void delete(Diagnose element) throws SQLException {
        String sql = "{call ADMIN.DIAGNOSE_PACK.DELETE_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getPatientId());

            statement.execute();
        }
    }

    @Override
    public void update(Diagnose element) throws SQLException {
        String sql = "{call ADMIN.DIAGNOSE_PACK.UPDATE_DIAGNOSE(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setInt(2, element.getDoctorId());
            statement.setInt(3, element.getPatientId());
            statement.setInt(4, element.getDiseaseId());

            statement.setDate(5, new java.sql.Date(element.getOpenDate().getTime()));

            if (element.getCloseDate() != null)
                statement.setDate(6, new java.sql.Date(element.getCloseDate().getTime()));
            else
                statement.setDate(6, null);

            statement.setString(7, element.getNote());
            statement.setString(8, element.getDescription());

            statement.setInt(9, element.getState().getValue());

            statement.execute();
        }
    }

    @Override
    public Diagnose get(Diagnose element) throws SQLException {
        List<Diagnose> diagnoses;

        String sql = "{call ? := ADMIN.DIAGNOSE_PACK.GET_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, element.getId());

            statement.execute();

            diagnoses = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diagnoses.get(0);
    }

    public List<Diagnose> getByUser(int userId) throws SQLException {
        List<Diagnose> diagnoses;

        String sql = "{call ? := ADMIN.DIAGNOSE_PACK.GET_DIAGNOSE_BY_USER(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, userId);

            statement.execute();

            diagnoses = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diagnoses;
    }

    @Override
    public List<Diagnose> getAll() throws SQLException {
        List<Diagnose> diagnoses;

        String sql = "{call ? := ADMIN.DIAGNOSE_PACK.GET_ALL_DIAGNOSES()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);

            statement.execute();

            diagnoses = parseResultSet(statement.getObject(1, ResultSet.class));
        }

        return diagnoses;
    }

    @Override
    protected List<Diagnose> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Diagnose> diagnoses = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("diagnose_id");

            int doctorId = resultSet.getInt("doctor_id");
            int patientId = resultSet.getInt("patient_id");
            int diseaseId = resultSet.getInt("disease_id");

            Date openDate = resultSet.getDate("open_date");
            Date closeDate = resultSet.getDate("close_date");

            String note = resultSet.getString("note");
            String description = resultSet.getString("description");

            DiagnoseState state = DiagnoseState.getByValue(resultSet.getInt("diagnose_state"));


            diagnoses.add(new Diagnose(id, patientId, doctorId, diseaseId, openDate, closeDate, note, description, state));
        }

        return diagnoses;
    }
}
