package com.medkit.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionController implements ErrorController {

    @GetMapping({ "/ErrorPage" })
    public ModelAndView errorPage(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("ErrorPage");

        modelAndView.addObject("error", request.getSession().getAttribute("lastError"));

        return modelAndView;
    }
}
