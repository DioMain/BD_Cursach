package com.medkit.session;

import com.medkit.exception.UnknownUserRoleException;
import com.medkit.model.User;
import com.medkit.model.UserRole;
import com.medkit.repository.UserRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
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

    private final UserRepository userRepository;

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

        userRepository = new UserRepository(connection);

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
