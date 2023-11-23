package com.medkit.filter;

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

@Slf4j
public class TokenFilter implements Filter {

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Cookie authKey = new Cookie("authKey", "");

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().compareTo("authKey") == 0)
                authKey = cookie;
        }

        JwtService jwt = new JwtService();

        if (!jwt.validateToken(authKey.getValue())) {
            authKey.setValue("");

            response.addCookie(authKey);

            log.info("Not valid jwt token!");

            response.setCharacterEncoding("UTF-8");
            response.sendRedirect("/Login");
        }
        else {
            String email = jwt.getUserEmailFromToken(authKey.getValue());
            String password = jwt.getUserPasswordFromToken(authKey.getValue());

            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            if (instance.login(email, password)){
                SessionManager.getInstance().createSession(instance.getCurrentUser().getUserRole(), request.getSession().getId());

                instance = SessionManager.getSession(request.getSession().getId());

                assert instance != null;

                instance.login(email, password);

                log.info("Create oracle session on [SID: " + request.getSession().getId() +
                        ", ROLE: " + instance.getCurrentUser().getUserRole() + "];");

                chain.doFilter(request, response);
            }
            else {
                log.info("Login failed!");

                response.setCharacterEncoding("UTF-8");
                response.sendRedirect("/Login");
            }
        }
    }
}