package com.medkit.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import java.io.IOException;
import java.lang.annotation.Annotation;

@WebFilter
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Логика фильтрации
        System.out.println("Executing Filter");

        // Продолжаем цепочку фильтров
        chain.doFilter(request, response);
    }
}