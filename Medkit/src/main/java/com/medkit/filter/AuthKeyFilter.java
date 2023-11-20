package com.medkit.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Arrays;

public class AuthKeyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        boolean hasAuthKey = Arrays.stream(request.getCookies()).anyMatch((Cookie cookie) -> {
           return cookie.getName().compareTo("authKey") == 0;
        });

        if (!hasAuthKey){
            response.addCookie(new Cookie("authKey", ""));
        }

        filterChain.doFilter(request, response);
    }
}
