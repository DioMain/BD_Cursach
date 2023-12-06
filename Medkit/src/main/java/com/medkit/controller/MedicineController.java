package com.medkit.controller;

import com.medkit.exception.SessionException;
import com.medkit.forms.IdForm;
import com.medkit.forms.MedicineForm;
import com.medkit.model.Medicine;
import com.medkit.session.SessionInstance;
import com.medkit.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MedicineController {

    @GetMapping({ "/app/AllMedicine" })
    public ModelAndView allMedicine(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("MedicineViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        mav.getModelMap().addAttribute("medicines", instance.getMedicineRepository().getAll());

        return mav;
    }

    @GetMapping(value = { "/app/MedicineEditor" }, params = { "id" })
    public ModelAndView medicineEditorGet(HttpServletRequest request, @RequestParam("id") int id) throws SQLException {
        ModelAndView mav = new ModelAndView("MedicineEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        MedicineForm form = new MedicineForm();

        if (id > 0) {
            Medicine medicine = instance.getMedicineRepository().getById(id);

            form.setId(medicine.getId());
            form.setName(medicine.getName());
            form.setDescription(medicine.getDescription());
            form.setManufacturer(medicine.getManufacturer());
            form.setPrice(medicine.getPrice());
            form.setStartDate(medicine.getStartDate().toString());
        }

        form.setId(id);

        mav.getModelMap().addAttribute("medicineForm", form);

        return mav;
    }

    @PostMapping(value = { "/app/MedicineEditor" })
    public ModelAndView medicineEditorGet(HttpServletRequest request,
                                          @Valid @ModelAttribute("medicineForm") MedicineForm form,
                                          BindingResult bindingResult) throws SQLException, ParseException {

        ModelAndView mav = new ModelAndView("redirect:/app/AllMedicine");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = simpleDateFormat.parse(form.getStartDate());

        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();

            bindingResult.getFieldErrors().forEach(i -> {
                error.append(i.getDefaultMessage()).append("<br>");
            });

            mav.setViewName("MedicineEditor");

            mav.getModelMap().addAttribute("medicineForm", form);
            mav.getModelMap().addAttribute("error", error);
        }
        else if (date.after(new Date())){
            mav.setViewName("MedicineEditor");

            mav.getModelMap().addAttribute("medicineForm", form);
            mav.getModelMap().addAttribute("error", "Не верная дата!");
        }
        else {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            Medicine medicine = new Medicine(form.getId(), form.getName(), form.getDescription(),
                                             form.getManufacturer(), form.getPrice(), date);

            if (medicine.getId() > 0)
                instance.getMedicineRepository().update(medicine);
            else
                instance.getMedicineRepository().insert(medicine);
        }

        return mav;
    }

    @DeleteMapping({ "/app/DeleteMedicine" })
    @Async
    public void deleteMedicine(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody IdForm form) throws IOException, SQLException {

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        instance.getMedicineRepository().delete(new Medicine(form.getId(), "", "", "", 0, new Date()));

        response.setContentType("text/plain");
        response.getWriter().write("ok");
    }
}
