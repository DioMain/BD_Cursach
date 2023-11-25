package com.medkit.service;

import com.medkit.model.Diagnose;
import com.medkit.model.Disease;
import com.medkit.model.Medicine;
import com.medkit.model.Symptom;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class OracleMTMConnectionService {
    private Connection connection;

    public void DiagnoseToMedicine(Diagnose diagnose, Medicine medicine) throws SQLException {

    }

    public void DiagnoseToSymptom(Diagnose diagnose, Symptom symptom)  throws SQLException {

    }

    public void DiseaseToSymptom(Disease disease, Symptom symptom)  throws SQLException {

    }
}
