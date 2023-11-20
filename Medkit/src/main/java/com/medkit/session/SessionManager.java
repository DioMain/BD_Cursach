package com.medkit.session;

import com.medkit.exception.SessionException;
import com.medkit.model.UserRole;
import lombok.Getter;
import org.springframework.stereotype.Component;


import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

@Getter
@Component
public class SessionManager {

    @Getter
    private static SessionManager instance;

    private final String connectionString;

    private final String adminName;
    private final String adminPassword;

    private final String doctorName;
    private final String doctorPassword;

    private final String patientName;
    private final String patientPassword;

    private final String logregName;
    private final String logregPassword;

    private final Dictionary<String, SessionInstance> sessionInstances;

    public SessionManager() {
        sessionInstances = new Hashtable<>();

        connectionString = "jdbc:oracle:thin:@//DESKTOP-SE50MB3:1521/MEDKIT_PDB";

        adminName = "sys as sysdba";
        adminPassword = "1234";

        patientName = "PATIENT";
        patientPassword = "PAT";

        doctorName = "DOCTOR";
        doctorPassword = "DOC";

        logregName = "LOGREG";
        logregPassword = "LR";
    }

    public void createSession(UserRole role, String httpSessionId) throws SessionException {
        try {
            sessionInstances.put(httpSessionId, new SessionInstance(role));
        } catch (SQLException sqlException) {
            throw new SessionException("Error on session creation [" + httpSessionId + "], SQL: " + sqlException.getMessage());
        } catch (Exception error) {
            throw new SessionException("Error on session creation [" + httpSessionId + "]");
        }
    }

    public void deleteSession(String httpSessionId) {
        if (sessionIsExist(httpSessionId))
            sessionInstances.remove(httpSessionId);
    }

    public boolean sessionIsExist(String httpSessionId) {
        return sessionInstances.get(httpSessionId) != null;
    }

    public void cleanup() {

    }

    public static void createInstance() {


        instance = new SessionManager();
    }

    public static SessionInstance getSession(String httpSessionId) {
        if (!instance.sessionIsExist(httpSessionId))
            return null;

        return instance.sessionInstances.get(httpSessionId);
    }
}
