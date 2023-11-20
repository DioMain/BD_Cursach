package com.medkit.listener;

import com.medkit.exception.SessionException;
import com.medkit.model.UserRole;
import com.medkit.service.JwtService;
import com.medkit.session.SessionManager;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@WebListener
@Slf4j
public class Listener implements HttpSessionListener {
    @Value("${server.session.timeout}")
    private int sessionTimeout;

    @SneakyThrows
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log.info("Session created: [" + event.getSession().getId() + "];");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SessionManager.getInstance().deleteSession(event.getSession().getId());

        log.info("Session removed: [" + event.getSession().getId() + "];");
    }
}
