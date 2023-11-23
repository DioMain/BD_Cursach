package com.medkit.controller;

import com.medkit.exception.SessionException;
import com.medkit.forms.UserEditorForm;
import com.medkit.model.User;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class UserEditorController {
    @ExceptionHandler(SessionException.class)
    public ModelAndView sessionTrow(Throwable exception, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ErrorPage");

        request.getSession().setAttribute("lastError", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView anyTrow(Throwable exception, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:/ErrorPage");

        request.getSession().setAttribute("lastError", exception.getMessage());

        return modelAndView;
    }

    @GetMapping({ "/app/UserEditor" })
    public ModelAndView userEditorPage(Model model, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("UserEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        mav.getModelMap().addAttribute("user", instance.getCurrentUser());
        mav.getModelMap().addAttribute("userForm", new UserEditorForm());
        mav.getModelMap().addAttribute("error", "");

        return mav;
    }

    @PostMapping({ "/app/UserEditor_post" })
    public ModelAndView userEditorPost(Model model, HttpServletRequest request,
                                       @Valid @ModelAttribute("userForm") UserEditorForm form,
                                       BindingResult validationResult) throws ParseException, SQLException {

        ModelAndView mav = new ModelAndView();

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (validationResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();

            validationResult.getFieldErrors().forEach(i -> {
                errors.append(i.getDefaultMessage()).append(" \n");
            });

            mav.getModelMap().addAttribute("error", errors);

            mav.setViewName("UserEditor");
        }
        else {
            User newUser = getUser(form, instance);

            instance.getUserRepository().update(newUser);

            mav.setViewName("redirect:/app/MainPage");
        }

        return mav;
    }

    private static User getUser(UserEditorForm form, SessionInstance instance) throws ParseException {
        User currentUser = instance.getCurrentUser();

        User newUser = new User();

        newUser.setId(currentUser.getId());
        newUser.setEmail(currentUser.getEmail());

        newUser.setUserRole(currentUser.getUserRole());

        newUser.setName(form.getName());
        newUser.setSurname(form.getSurname());
        newUser.setPassword("");
        newUser.setPatronymic(form.getPatronymic());
        newUser.setPhoneNumber(form.getPhoneNumber());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        newUser.setBirthday(simpleDateFormat.parse(form.getBirthday()));
        return newUser;
    }
}
