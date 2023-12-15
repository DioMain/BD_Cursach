package com.medkit.filter;

import com.medkit.model.UserRole;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PatientFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (instance.getCurrentUser().getUserRole() != UserRole.PATIENT) {
            response.setCharacterEncoding("UTF-8");
            response.sendRedirect("/app/MainPage");
        }

        filterChain.doFilter(request, response);
    }
}
