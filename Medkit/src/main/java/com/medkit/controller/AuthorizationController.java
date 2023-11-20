package com.medkit.controller;

import com.medkit.form.LoginForm;
import com.medkit.model.User;
import com.medkit.model.UserRole;
import com.medkit.exception.SessionException;

import com.medkit.service.JwtService;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
public class AuthorizationController {

    @ExceptionHandler(Exception.class)
    public ModelAndView sessionException(SessionException exception, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ErrorPage");

        request.getSession().setAttribute("lastError", exception.getMessage());

        return modelAndView;
    }

    @GetMapping(value = {"/Login", "/index", "/"})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws SessionException {
        ModelAndView mav = new ModelAndView();

        createSession(request);

        response.setHeader("Content-Type", "text/html; charset=utf-8");

        mav.setViewName("Login");

        return mav;
    }

    @GetMapping(value = {"/Registration"})
    public ModelAndView registration(HttpServletRequest request, HttpServletResponse response) throws SessionException {
        ModelAndView mav = new ModelAndView();

        createSession(request);

        response.setHeader("Content-Type", "text/html; charset=utf-8");

        mav.setViewName("Registration");

        return mav;
    }

    @PostMapping(value = { "/api/login"})
    public ModelAndView loginApi(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("loginForm")LoginForm form) {
        ModelAndView mav = new ModelAndView();

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (instance.login(form.getEmail(), form.getPassword())) {
            JwtService jwt = new JwtService();

            response.addCookie(new Cookie("authKey",
                    jwt.generateToken(instance.getCurrentUser().getEmail(),
                                      form.getPassword())));

            response.setHeader("Content-Type", "text/html; charset=utf-8");

            mav.setViewName("MainPage");
        }
        else {
            request.getSession().setAttribute("error", "Не верная почта или пароль!");

            response.setHeader("Content-Type", "text/html; charset=utf-8");

            mav.setViewName("Login");
        }

        return mav;
    }

    private void createSession(HttpServletRequest request) throws SessionException {
        HttpSession session = request.getSession(false);

        if (session == null)
            session = request.getSession(true);

        SessionManager.getInstance().createSession(UserRole.LOGIN_REGISTRATION, session.getId());

        log.info("Create oracle session on [SID: " + request.getSession().getId() +
                ", ROLE: LOGREG];");
    }
}
