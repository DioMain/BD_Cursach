package com.medkit.controller;

import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
public class MainPageController {
    @GetMapping({ "/app/MainPage" })
    public ModelAndView mainPage(HttpServletRequest request){
        //response.setHeader("Content-Type", "text/html; charset=utf-8");

        ModelAndView mav = new ModelAndView("MainPage");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        mav.getModelMap().addAttribute("user", instance.getCurrentUser());

        return mav;
    }

    @GetMapping({ "/app/Exit" })
    public ModelAndView exit(HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("redirect:/Login");

        Cookie cookie = new Cookie("authKey", "");

        cookie.setPath("/");
        cookie.setMaxAge(3000);

        response.addCookie(cookie);

        return mav;
    }

    @GetMapping({ "/app/DeleteUser" })
    public ModelAndView deleteUser(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("redirect:/Login");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        instance.getUserRepository().delete(instance.getCurrentUser());

        return mav;
    }
}
