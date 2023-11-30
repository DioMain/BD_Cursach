package com.medkit.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medkit.forms.DiseaseEditorForm;
import com.medkit.forms.DiseaseViewForm;
import com.medkit.forms.IdForm;
import com.medkit.forms.SymptomForm;
import com.medkit.model.DiagnoseToSymptom;
import com.medkit.model.Disease;
import com.medkit.model.DiseaseToSymptom;
import com.medkit.model.Symptom;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DiseaseController {
    @GetMapping({ "/app/DiseaseViewer" })
    public ModelAndView diseaseViewer(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("DiseaseViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<DiseaseViewForm> forms = new ArrayList<>();

        for (Disease disease : instance.getDiseaseRepository().getAll()) {

            List<Symptom> symptoms = new ArrayList<>();
            for (DiseaseToSymptom dts : instance.getMTMConnector().getDiseaseToSymptomByDisease(disease.getId())) {
                symptoms.add(instance.getSymptomRepository().getById(dts.getSymptomId()));
            }

            forms.add(new DiseaseViewForm(disease.getId(), disease.getName(), disease.getDescription(), symptoms));
        }

        mav.getModelMap().addAttribute("diseases", forms);

        return mav;
    }

    @GetMapping(value = { "/app/DiseaseEditor" }, params = { "id" })
    public ModelAndView diseaseEditor (HttpServletRequest request,
                                       @RequestParam("id") int id) throws SQLException, JsonProcessingException {

        ModelAndView mav = new ModelAndView("DiseaseEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        DiseaseEditorForm form = new DiseaseEditorForm(0, "", "", "[]");

        ObjectMapper mapper = new ObjectMapper();

        if (id > 0) {
            Disease disease = instance.getDiseaseRepository().getById(id);

            form = new DiseaseEditorForm(disease.getId(), disease.getName(), disease.getDescription(), "[]");

            List<Symptom> symptoms = new ArrayList<>();
            for (DiseaseToSymptom dts : instance.getMTMConnector().getDiseaseToSymptomByDisease(id)) {
                symptoms.add(instance.getSymptomRepository().getById(dts.getSymptomId()));
            }

            form.setSymptomsJson(mapper.writeValueAsString(symptoms));
        }

        mav.getModelMap().addAttribute("diseaseForm", form);
        mav.getModelMap().addAttribute("symptoms", instance.getSymptomRepository().getAll());
        mav.getModelMap().addAttribute("symptomsJson", mapper.writeValueAsString(instance.getSymptomRepository().getAll()));

        return mav;
    }

    @PostMapping(value = { "/app/DiseaseEditor" })
    @Async
    public void diseaseEditorGet(HttpServletRequest request, HttpServletResponse response,
                                 @Valid @RequestBody DiseaseEditorForm form,
                                 BindingResult bindingResult) throws SQLException, ParseException, IOException {
        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();

            bindingResult.getFieldErrors().forEach(i -> {
                error.append(i.getDefaultMessage()).append("<br>");
            });

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(error.toString());
        }
        else {
            ObjectMapper mapper = new ObjectMapper();

            List<Symptom> symptoms = mapper.readValue(form.getSymptomsJson() , new TypeReference<List<Symptom>>() { });

            Disease disease = new Disease();

            disease.setId(form.getId());
            disease.setName(form.getName());
            disease.setDescription(form.getDescription());

            if (form.getId() > 0)
                instance.getDiseaseRepository().update(disease);
            else
                disease = instance.getDiseaseRepository().insertWithReturn(disease);

            instance.getMTMConnector().deleteDiseaseToSymptomByDisease(form.getId());

            for (Symptom item : symptoms) {
                instance.getMTMConnector().diseaseToSymptom(disease, item);
            }

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("ok");
        }
    }

    @DeleteMapping({ "/app/DeleteDisease" })
    @Async
    public void deleteMedicine(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody IdForm form) throws IOException, SQLException {

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        instance.getDiseaseRepository().delete(new Disease(form.getId(), "", ""));

        response.setContentType("text/plain");
        response.getWriter().write("ok");
    }
}
