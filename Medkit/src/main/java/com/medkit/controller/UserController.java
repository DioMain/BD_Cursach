package com.medkit.controller;

import com.medkit.forms.IdForm;
import com.medkit.forms.UserEditorForm;
import com.medkit.model.User;
import com.medkit.model.UserRole;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @GetMapping({ "/app/UserEditor" })
    public ModelAndView userEditorPage(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("UserEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        User user = instance.getCurrentUser();

        UserEditorForm form = new UserEditorForm();

        form.setName(user.getName());
        form.setSurname(user.getSurname());
        form.setPatronymic(user.getPatronymic());

        form.setBirthday(user.getBirthday().toString());
        form.setPhoneNumber(user.getPhoneNumber());

        mav.getModelMap().addAttribute("userForm", form);
        mav.getModelMap().addAttribute("error", "");

        return mav;
    }

    @PostMapping({ "/app/UserEditor_post" })
    public ModelAndView userEditorPost(HttpServletRequest request,
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

    @GetMapping(value = { "/app/UserViewer" }, params = { "filter" })
    public ModelAndView userViewer(HttpServletRequest request,
                                   @RequestParam("filter") String filter) throws SQLException {

        ModelAndView mav = new ModelAndView("UserViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<User> users;

        if (filter.compareTo("") == 0)
            users = instance.getUserRepository().getFirst(50);
        else {
            String[] arr = filter.split(" ", 3);;

            if (Arrays.stream(arr).count() == 1)
                users = instance.getUserRepository().getByName("", arr[0], "");
            else if (Arrays.stream(arr).count() == 2)
                users = instance.getUserRepository().getByName(arr[1], arr[0], "");
            else
                users = instance.getUserRepository().getByName(arr[1], arr[0], arr[2]);
        }

        mav.getModelMap().addAttribute("users", users);

        return mav;
    }

    @DeleteMapping({ "/app/DeleteUser_V" })
    @Async
    public void deleteUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody IdForm form) throws IOException, SQLException {

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        instance.getUserRepository().delete(new User(form.getId(), UserRole.LOGIN_REGISTRATION, "", "", "", "", "", "", new Date()));

        response.setContentType("text/plain");
        response.getWriter().write("ok");
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
