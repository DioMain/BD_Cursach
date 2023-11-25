package com.medkit.filter;

import com.medkit.exception.SessionException;
import com.medkit.model.UserRole;
import com.medkit.service.JwtService;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Cookie authKey = new Cookie("authKey", "");

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().compareTo("authKey") == 0)
                authKey = cookie;
        }

        JwtService jwt = new JwtService();

        if (!jwt.validateToken(authKey.getValue())) {
            log.info("Not valid jwt token! FOR: [SID: " + request.getSession().getId() + "]");

            response.setCharacterEncoding("UTF-8");
            response.sendRedirect("/Login");
        }
        else {
            String email = jwt.getUserEmailFromToken(authKey.getValue());
            String password = jwt.getUserPasswordFromToken(authKey.getValue());

            try {
                SessionInstance instance = SessionManager.getSession(request.getSession().getId());

                if (instance == null){
                    SessionManager.getInstance().createSession(UserRole.LOGIN_REGISTRATION, request.getSession().getId());

                    instance = SessionManager.getSession(request.getSession().getId());
                }

                assert instance != null;
                if (instance.login(email, password)){

                    UserRole currentRole = instance.getCurrentUser().getUserRole();
                    String currentEmail = instance.getCurrentUser().getEmail();

                    if (currentRole != instance.getConnectionRole() || currentEmail.compareTo(email) != 0) {
                        SessionManager.getInstance().createSession(instance.getCurrentUser().getUserRole(), request.getSession().getId());

                        instance = SessionManager.getSession(request.getSession().getId());

                        assert instance != null;

                        instance.login(email, password);

                        log.info("Create oracle session on [SID: " + request.getSession().getId() +
                                ", ROLE: " + instance.getCurrentUser().getUserRole() + "];");
                    }

                    chain.doFilter(request, response);
                }
                else {
                    log.warn("Login failed!");

                    response.setCharacterEncoding("UTF-8");
                    response.sendRedirect("/Login");
                }
            } catch (SessionException e) {
                log.error(e.getMessage());

                request.getSession().setAttribute("lastError", e.getMessage());

                response.setCharacterEncoding("UTF-8");
                response.sendRedirect("/ErrorPage");
            }
        }
    }
}