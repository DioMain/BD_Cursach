package com.medkit.controller;

import com.medkit.forms.IdForm;
import com.medkit.forms.MedicineForm;
import com.medkit.forms.SymptomForm;
import com.medkit.model.Medicine;
import com.medkit.model.Symptom;
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
import java.util.Date;

@Controller
public class SymptomController {
    @GetMapping({ "/app/SymptomViewer" })
    public ModelAndView symptomViewer(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("SymptomViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        mav.getModelMap().addAttribute("symptoms", instance.getSymptomRepository().getAll());

        return mav;
    }

    @GetMapping(value = { "/app/SymptomEditor" }, params = { "id" })
    public ModelAndView symptomEditor (HttpServletRequest request,
                                       @RequestParam("id") int id) throws SQLException {

        ModelAndView mav = new ModelAndView("SymptomEditor");

        SymptomForm form = new SymptomForm();

        if (id > 0) {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            Symptom symptom = instance.getSymptomRepository().getById(id);

            form = new SymptomForm(symptom.getId(), symptom.getName(), symptom.getDescription());
        }

        mav.getModelMap().addAttribute("symptomForm", form);

        return mav;
    }

    @PostMapping(value = { "/app/SymptomEditor" })
    public ModelAndView medicineEditorGet(HttpServletRequest request,
                                          @Valid @ModelAttribute("symptomForm") SymptomForm form,
                                          BindingResult bindingResult) throws SQLException, ParseException {

        ModelAndView mav = new ModelAndView("redirect:/app/SymptomViewer");

        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();

            bindingResult.getFieldErrors().forEach(i -> {
                error.append(i.getDefaultMessage()).append("<br>");
            });

            mav.setViewName("SymptomEditor");

            mav.getModelMap().addAttribute("symptomForm", form);
            mav.getModelMap().addAttribute("error", error);
        }
        else {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            Symptom symptom = new Symptom(form.getId(), form.getName(), form.getDescription());

            if (symptom.getId() > 0)
                instance.getSymptomRepository().update(symptom);
            else
                instance.getSymptomRepository().insert(symptom);
        }

        return mav;
    }

    @DeleteMapping({ "/app/DeleteSymptom" })
    @Async
    public void deleteMedicine(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody IdForm form) throws IOException, SQLException {

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        instance.getSymptomRepository().delete(new Symptom(form.getId(), "", ""));

        response.setContentType("text/plain");
        response.getWriter().write("ok");
    }
}
