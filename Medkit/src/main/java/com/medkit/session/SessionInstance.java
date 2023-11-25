package com.medkit.session;

import com.medkit.exception.UnknownUserRoleException;
import com.medkit.model.*;
import com.medkit.repository.*;
import com.medkit.service.OracleMTMConnectionService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Getter
public class SessionInstance implements Closeable {
    private final Connection connection;

    private User currentUser;

    private final UserRole connectionRole;

    private final OracleMTMConnectionService MTMConnector;

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final DiseaseRepository diseaseRepository;
    private final MedicineRepository medicineRepository;
    private final SymptomRepository symptomRepository;

    public SessionInstance(UserRole role) throws UnknownUserRoleException, SQLException {

        switch (role) {
            case ADMIN -> {
                connection = createConnection(SessionManager.getInstance().getAdminName(),
                                              SessionManager.getInstance().getAdminPassword());
            }
            case DOCTOR -> {
                connection = createConnection(SessionManager.getInstance().getDoctorName(),
                                              SessionManager.getInstance().getDoctorPassword());
            }
            case PATIENT -> {
                connection = createConnection(SessionManager.getInstance().getPatientName(),
                                              SessionManager.getInstance().getPatientPassword());
            }
            case LOGIN_REGISTRATION -> {
                connection = createConnection(SessionManager.getInstance().getLogregName(),
                                              SessionManager.getInstance().getLogregPassword());
            }
            default -> throw new UnknownUserRoleException(role);
        }

        MTMConnector = new OracleMTMConnectionService(connection);

        userRepository = new UserRepository(connection);
        appointmentRepository = new AppointmentRepository(connection);
        diagnoseRepository = new DiagnoseRepository(connection);
        diseaseRepository = new DiseaseRepository(connection);
        medicineRepository = new MedicineRepository(connection);
        symptomRepository = new SymptomRepository(connection);

        connectionRole = role;
    }

    public boolean login(String email, String password) {
        try {
            currentUser = userRepository.login(email, password);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private Connection createConnection(String name, String password) throws SQLException {
        return DriverManager.getConnection(SessionManager.getInstance().getConnectionString(), name, password);
    }
}
