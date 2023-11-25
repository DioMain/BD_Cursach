package com.medkit.controller;

import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

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

    @DeleteMapping({ "/app/DeleteUser" })
    @Async
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain");
        try {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            instance.getUserRepository().delete(instance.getCurrentUser());

            response.getWriter().write("Ok");
        }
        catch (SQLException e) {
            response.getWriter().write(e.getMessage());
        }
    }
}
