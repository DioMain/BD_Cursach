package com.medkit.session2;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SessionManager {

    @Getter
    private static SessionManager instance;

    @Value("${database.connectionString}")
    private String connectionString;

    @Value("${database.admin.name}")
    private String adminName;
    @Value("${database.admin.password}")
    private String adminPassword;

    @Value("${database.doctor.name}")
    private String doctorName;
    @Value("${database.doctor.password}")
    private String doctorPassword;

    @Value("${database.patient.name}")
    private String patientName;
    @Value("${database.patient.password}")
    private String patientPassword;

    @Value("${database.logreg.name}")
    private String logregName;
    @Value("${database.logreg.password}")
    private String logregPassword;

    private final List<SessionInstance> sessionInstances;

    public SessionManager() {
        sessionInstances = new ArrayList<>();
    }

    //public void createSession

    public void cleanup() {

    }

    public static void CreateInstance() {
        instance = new SessionManager();
    }
}
