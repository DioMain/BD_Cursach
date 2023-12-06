package com.medkit.controller;

import com.medkit.forms.LoginForm;
import com.medkit.forms.RegistrationForm;
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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
public class AuthorizationController {

    @GetMapping(value = {"/Login", "/index", "/"})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws SessionException {
        ModelAndView mav = new ModelAndView();

        createSession(request);

        response.setHeader("Content-Type", "text/html; charset=utf-8");

        mav.getModelMap().addAttribute("loginForm", new LoginForm());

        mav.setViewName("Login");

        return mav;
    }

    @GetMapping(value = {"/Registration"})
    public ModelAndView registration(HttpServletRequest request, HttpServletResponse response) throws SessionException {
        ModelAndView mav = new ModelAndView();

        createSession(request);

        response.setHeader("Content-Type", "text/html; charset=utf-8");

        mav.getModelMap().addAttribute("registrationForm", new RegistrationForm());

        mav.setViewName("Registration");

        return mav;
    }

    @PostMapping(value = { "/api/login"})
    public ModelAndView loginApi(HttpServletRequest request, HttpServletResponse response,
                                 @ModelAttribute("loginForm") LoginForm form) {

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (instance.login(form.getEmail(), form.getPassword())) {
            JwtService jwt = new JwtService();

            Cookie cookie = new Cookie("authKey",
                    jwt.generateToken(instance.getCurrentUser().getEmail(),
                            form.getPassword()));

            cookie.setPath("/");
            cookie.setMaxAge(3000);

            response.addCookie(cookie);

            return new ModelAndView("redirect:/app/MainPage");
        }
        else {
            ModelAndView mav = new ModelAndView("Login");

            mav.getModelMap().addAttribute("error", "Не верная почта или пароль!");

            return mav;
        }
    }

    @PostMapping(value = {"/api/registration"})
    public ModelAndView registrationApi(HttpServletRequest request, HttpServletResponse response,
                                        @Valid @ModelAttribute("registrationForm") RegistrationForm form,
                                        BindingResult bindingResult) throws SessionException, ParseException {

        ModelAndView mav = new ModelAndView("Registration");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = simpleDateFormat.parse(form.getBirthday());

        if (bindingResult.hasErrors()){
            StringBuilder error = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                error.append(fieldError.getDefaultMessage()).append("<br>");
            }

            mav.getModelMap().addAttribute("error", error);
        }
        else if (form.getPassword().compareTo(form.getConfirmPassword()) != 0){
            mav.getModelMap().addAttribute("error", "Пароли не совпадают!");
        }
        else if (date.after(new Date())) {
            mav.getModelMap().addAttribute("error", "Не верная дата!");
        }
        else {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            User newUser = getUser(form);

            try {
                instance.getUserRepository().insert(newUser);

                mav.getModelMap().addAttribute("registrationSuccess", true);
            }
            catch (SQLException sqlException) {
                mav.getModelMap().addAttribute("error", sqlException.getMessage());
            }
        }

        return mav;
    }

    private static User getUser(RegistrationForm form) throws ParseException {
        User newUser = new User();
        newUser.setName(form.getName());
        newUser.setSurname(form.getSurname());
        newUser.setPatronymic(form.getPatronymic());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        newUser.setBirthday(simpleDateFormat.parse(form.getBirthday()));


        newUser.setUserRole(UserRole.getByValue(form.getRole()));
        newUser.setPhoneNumber(form.getPhoneNumber());
        newUser.setEmail(form.getEmail());
        newUser.setPassword(form.getPassword());
        return newUser;
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
