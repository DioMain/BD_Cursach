package com.medkit.service;

import com.medkit.model.*;
import lombok.AllArgsConstructor;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class OracleMTMConnectionService {
    private Connection connection;

    public void diagnoseToMedicine(Diagnose diagnose, Medicine medicine) throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DIAGNOSES_TO_MEDICINES(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, diagnose.getId());
            statement.setInt(2, medicine.getId());

            statement.execute();
        }
    }

    public void diagnoseToSymptom(Diagnose diagnose, Symptom symptom)  throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DIAGNOSES_TO_SYMPTOMS(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, diagnose.getId());
            statement.setInt(2, symptom.getId());

            statement.execute();
        }
    }

    public void diseaseToSymptom(Disease disease, Symptom symptom)  throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DISEASES_TO_SYMPTOMS(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, disease.getId());
            statement.setInt(2, symptom.getId());

            statement.execute();
        }
    }

    public List<DiseaseToSymptom> getDiseaseToSymptomByDisease(int diseaseId) throws SQLException {
        List<DiseaseToSymptom> list = new ArrayList<>();

        String sql = "{call ? := ADMIN.MTM_PACK.GET_DISEASESTOSYMPTOMS_BY_DISEASE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, diseaseId);

            statement.execute();

            list = parseResultSetToDiseaseToSymptom(statement.getObject(1, ResultSet.class));
        }

        return list;
    }

    public List<DiagnoseToSymptom> getDiagnoseToSymptomByDiagnose(int diagnoseId) throws SQLException {
        List<DiagnoseToSymptom> list = new ArrayList<>();

        String sql = "{call ? := ADMIN.MTM_PACK.GET_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, diagnoseId);

            statement.execute();

            list = parseResultSetToDiagnoseToSymptom(statement.getObject(1, ResultSet.class));
        }

        return list;
    }

    public List<DiagnoseToMedicine> getDiagnoseToMedicineByDiagnose(int diagnoseId) throws SQLException {
        List<DiagnoseToMedicine> list = new ArrayList<>();

        String sql = "{call ? := ADMIN.MTM_PACK.GET_DIAGNOSESTOMEDICINES_BY_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setInt(2, diagnoseId);

            statement.execute();

            list = parseResultSetToDiagnoseToMedicine(statement.getObject(1, ResultSet.class));
        }

        return list;
    }

    public void deleteDiagnoseToSymptomByDiagnose(int diagnoseId) throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DELETE_DIAGNOSESTOSYMPTOMS_BY_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, diagnoseId);

            statement.execute();
        }
    }

    public void deleteDiagnoseToMedicineByDiagnose(int diagnoseId) throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DELETE_DIAGNOSESTOMEDICINES_BY_DIAGNOSE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, diagnoseId);

            statement.execute();
        }
    }

    public void deleteDiseaseToSymptomByDisease(int diseaseId) throws SQLException {
        String sql = "{call ADMIN.MTM_PACK.DELETE_DISEASESTOSYMPTOMS_BY_DISEASE(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, diseaseId);

            statement.execute();
        }
    }


    private List<DiseaseToSymptom> parseResultSetToDiseaseToSymptom(ResultSet resultSet) throws SQLException {
        List<DiseaseToSymptom> list = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int diseaseId = resultSet.getInt("disease_id");
            int symptomId = resultSet.getInt("symptom_id");

            list.add(new DiseaseToSymptom(id, diseaseId, symptomId));
        }

        return list;
    }

    private List<DiagnoseToSymptom> parseResultSetToDiagnoseToSymptom(ResultSet resultSet) throws SQLException {
        List<DiagnoseToSymptom> list = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int diseaseId = resultSet.getInt("diagnose_id");
            int symptomId = resultSet.getInt("symptom_id");

            list.add(new DiagnoseToSymptom(id, diseaseId, symptomId));
        }

        return list;
    }

    private List<DiagnoseToMedicine> parseResultSetToDiagnoseToMedicine(ResultSet resultSet) throws SQLException {
        List<DiagnoseToMedicine> list = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int diseaseId = resultSet.getInt("diagnose_id");
            int symptomId = resultSet.getInt("medicine_id");

            list.add(new DiagnoseToMedicine(id, diseaseId, symptomId));
        }

        return list;
    }

}
