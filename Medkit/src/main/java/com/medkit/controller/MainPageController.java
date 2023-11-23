package com.medkit.controller;

import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainPageController {
    @GetMapping({ "/app/MainPage" })
    public ModelAndView mainPage(Model model, HttpServletRequest request){
        //response.setHeader("Content-Type", "text/html; charset=utf-8");

        ModelAndView mav = new ModelAndView("MainPage");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        mav.getModelMap().addAttribute("user", instance.getCurrentUser());

        return mav;
    }
}
