package com.medkit.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medkit.forms.*;
import com.medkit.model.*;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class DiagnoseController {
    @GetMapping(value = { "/app/DiagnoseViewer" }, params = { "filter" })
    public ModelAndView diagnoseViewer(HttpServletRequest request,
                                      @RequestParam("filter") int filter) throws SQLException {
        ModelAndView mav = new ModelAndView("DiagnoseViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<DiagnoseViewForm> forms = new ArrayList<>();

        for (Diagnose diagnose : instance.getDiagnoseRepository().getByUser(instance.getCurrentUser().getId())) {

            if (filter == 1 && diagnose.getState() != DiagnoseState.OPENED) {
                continue;
            }

            List<Symptom> symptoms = new ArrayList<>();
            for (DiagnoseToSymptom dts : instance.getMTMConnector().getDiagnoseToSymptomByDiagnose(diagnose.getId())) {
                symptoms.add(instance.getSymptomRepository().getById(dts.getSymptomId()));
            }

            List<Medicine> medicines = new ArrayList<>();
            for (DiagnoseToMedicine dtm : instance.getMTMConnector().getDiagnoseToMedicineByDiagnose(diagnose.getId())) {
                medicines.add(instance.getMedicineRepository().getById(dtm.getMedicineId()));
            }

            Disease disease = instance.getDiseaseRepository().getById(diagnose.getDiseaseId());

            User doctor = instance.getUserRepository().getById(diagnose.getDoctorId());
            User patient = instance.getUserRepository().getById(diagnose.getPatientId());


            String closeDate;

            if (diagnose.getCloseDate() == null)
                closeDate = "";
            else
                closeDate = diagnose.getCloseDate().toString();

            forms.add(new DiagnoseViewForm(diagnose.getId(), diagnose.getOpenDate().toString(), closeDate,
                    diagnose.getDescription(), diagnose.getNote(), disease, patient, doctor, diagnose.getState().getValue(), symptoms, medicines));
        }

        mav.getModelMap().addAttribute("diagnoses", forms);
        mav.getModelMap().addAttribute("user", instance.getCurrentUser());

        return mav;
    }

    @GetMapping(value = { "/app/DiagnoseEditor"}, params = { "id" })
    public ModelAndView diagnoseEditor(HttpServletRequest request,
                                       @RequestParam("id") int id) throws SQLException, JsonProcessingException {

        ModelAndView mav = new ModelAndView("DiagnoseEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        ObjectMapper mapper = new ObjectMapper();

        DiagnoseInEditorForm form = new DiagnoseInEditorForm(0, new Date().toString(), "", "", "", "", "",
                                                                mapper.writeValueAsString(instance.getCurrentUser()),
                                                                DiagnoseState.OPENED.getValue(), "[]", "[]");

        if (id > 0) {
            Diagnose diagnose = instance.getDiagnoseRepository().getById(id);

            form.setId(diagnose.getId());
            form.setNote(diagnose.getNote());
            form.setDescription(diagnose.getDescription());

            Disease disease = null;

            User patient = null;

            try {
                patient = instance.getUserRepository().getById(diagnose.getPatientId());
            }
            catch (Throwable ignored) { }

            try {
                disease = instance.getDiseaseRepository().getById(diagnose.getDiseaseId());
            }
            catch (Throwable ignored) { }

            form.setDiseaseJson(mapper.writeValueAsString(disease));
            form.setPatientJson(mapper.writeValueAsString(patient));

            form.setOpenDate(diagnose.getOpenDate().toString());
            form.setCloseDate("");

            List<Symptom> symptoms = new ArrayList<>();
            for (DiagnoseToSymptom dts : instance.getMTMConnector().getDiagnoseToSymptomByDiagnose(id)) {
                symptoms.add(instance.getSymptomRepository().getById(dts.getSymptomId()));
            }

            List<Medicine> medicines = new ArrayList<>();
            for (DiagnoseToMedicine dts : instance.getMTMConnector().getDiagnoseToMedicineByDiagnose(id)) {
                medicines.add(instance.getMedicineRepository().getById(dts.getMedicineId()));
            }

            form.setSymptomsJSON(mapper.writeValueAsString(symptoms));
            form.setMedicinesJSON(mapper.writeValueAsString(medicines));
        }

        mav.getModelMap().addAttribute("diagnoseForm", form);
        mav.getModelMap().addAttribute("symptoms", instance.getSymptomRepository().getAll());
        mav.getModelMap().addAttribute("medicines", instance.getMedicineRepository().getAll());
        mav.getModelMap().addAttribute("allSymptoms", mapper.writeValueAsString(instance.getSymptomRepository().getAll()));
        mav.getModelMap().addAttribute("allMedicines", mapper.writeValueAsString(instance.getMedicineRepository().getAll()));

        return mav;
    }

    @PostMapping({ "/app/DiagnoseGetUsers" })
    @Async
    public void getPatients(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody StringFrom form) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        try {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            List<User> users;

            String[] arr = form.getValue().split(" ", 3);

            if (form.getValue().isEmpty())
                users = new ArrayList<>();
            else if (Arrays.stream(arr).count() == 1)
                users = instance.getUserRepository().getByNameAndRole("", arr[0], "", UserRole.PATIENT);
            else if (Arrays.stream(arr).count() == 2)
                users = instance.getUserRepository().getByNameAndRole(arr[1], arr[0], "", UserRole.PATIENT);
            else
                users = instance.getUserRepository().getByNameAndRole(arr[1], arr[0], arr[2], UserRole.PATIENT);

            users = users.stream().limit(300).toList();

            response.getWriter().write(mapper.writeValueAsString(users));
        }
        catch (SQLException e) {
            response.getWriter().write(e.getMessage());
        }
    }

    @PostMapping({ "/app/DiagnoseGetDiseases"})
    @Async
    public void getDiseases(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody List<Symptom> symptoms) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        try {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            List<Disease> diseases = new ArrayList<>();

            if (!symptoms.isEmpty()) {
                for (Disease disease : instance.getDiseaseRepository().getAll()) {
                    List<DiseaseToSymptom> links = instance.getMTMConnector().getDiseaseToSymptomByDisease(disease.getId());

                    if (links.stream().anyMatch(
                            _link -> symptoms.stream().anyMatch(
                            _symptom -> _link.getSymptomId() == _symptom.getId()))) {

                        diseases.add(disease);
                    }
                }
            }
            else
                diseases = instance.getDiseaseRepository().getAll();

            response.getWriter().write(mapper.writeValueAsString(diseases));
        }
        catch (SQLException e) {
            response.getWriter().write(e.getMessage());
        }
    }

    @PostMapping({ "/app/DiagnoseEditor"})
    @Async
    public void diagnoseEditor_post(HttpServletRequest request, HttpServletResponse response,
                                    @Valid @RequestBody DiagnoseOutEditorForm form,
                                    BindingResult bindingResult) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();

            bindingResult.getFieldErrors().forEach(i -> {
                error.append(i.getDefaultMessage()).append("<br>");
            });

            response.getWriter().write(error.toString());
        }
        else {
            try {
                SessionInstance instance = SessionManager.getSession(request.getSession().getId());

                assert instance != null;

                Diagnose diagnose = new Diagnose();

                if (form.getId() > 0)
                    diagnose = instance.getDiagnoseRepository().getById(form.getId());
                else {
                    diagnose.setOpenDate(new Date());
                }

                if (form.getState() != 0)
                    diagnose.setCloseDate(new Date());

                diagnose.setNote(form.getNote());
                diagnose.setDescription(form.getDescription());
                diagnose.setState(DiagnoseState.getByValue(form.getState()));

                diagnose.setDoctorId(instance.getCurrentUser().getId());
                diagnose.setPatientId(form.getPatientId());

                diagnose.setDiseaseId(form.getDiseaseId());

                if (form.getId() > 0)
                    instance.getDiagnoseRepository().update(diagnose);
                else
                    diagnose = instance.getDiagnoseRepository().insertWithReturn(diagnose);

                instance.getMTMConnector().deleteDiagnoseToSymptomByDiagnose(diagnose.getId());
                instance.getMTMConnector().deleteDiagnoseToMedicineByDiagnose(diagnose.getId());

                List<Symptom> symptoms = mapper.readValue(form.getSymptomsJSON() , new TypeReference<List<Symptom>>() { });
                List<Medicine> medicines = mapper.readValue(form.getMedicinesJSON() , new TypeReference<List<Medicine>>() { });

                for (Symptom item : symptoms) {
                    instance.getMTMConnector().diagnoseToSymptom(diagnose, item);
                }

                for (Medicine item : medicines) {
                    instance.getMTMConnector().diagnoseToMedicine(diagnose, item);
                }

                response.getWriter().write("ok");
            }
            catch (SQLException e) {
                response.getWriter().write(e.getMessage());
            }
        }
    }
}
