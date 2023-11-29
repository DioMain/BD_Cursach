package com.medkit.controller;


import com.medkit.forms.DiagnoseViewForm;
import com.medkit.forms.DiseaseViewForm;
import com.medkit.model.*;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DiagnoseController {
    @GetMapping( value = { "/app/DiagnoseViewer" }, params = { "filter" })
    public ModelAndView diseaseViewer(HttpServletRequest request,
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

            forms.add(new DiagnoseViewForm(diagnose.getId(), diagnose.getOpenDate().toString(), diagnose.getCloseDate().toString(),
                    diagnose.getDescription(), diagnose.getNote(), disease, patient, doctor, diagnose.getState().getValue(), symptoms, medicines));
        }

        mav.getModelMap().addAttribute("diagnoses", forms);

        return mav;
    }
}
