package com.medkit.controller;

import com.medkit.exception.SessionException;
import com.medkit.forms.AppointmentForm;
import com.medkit.forms.AppointmentUpdateForm;
import com.medkit.forms.AppointmentViewForm;
import com.medkit.model.Appointment;
import com.medkit.model.AppointmentState;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class AppointmentController {
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

    @GetMapping({"/app/AppointmentEditor"})
    public ModelAndView appointmentEditor(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("AppointmentEditor");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        AppointmentForm form = new AppointmentForm();

        form.setPatientId(instance.getCurrentUser().getId());
        form.setState(0);

        mav.getModelMap().addAttribute("appointmentForm", form);
        mav.getModelMap().addAttribute("doctors", instance.getUserRepository().getByRole(UserRole.DOCTOR));

        return mav;
    }

    @PostMapping({ "/app/AppointmentEditor_post" })
    public ModelAndView appointmentInsert(HttpServletRequest request,
                                          @Valid @ModelAttribute("appointmentForm") AppointmentForm form,
                                          BindingResult bindingResult) throws ParseException, SQLException {
        ModelAndView mav = new ModelAndView();

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();

            bindingResult.getFieldErrors().forEach(i -> {
                error.append(i.getDefaultMessage()).append('\n');
            });

            mav.getModelMap().addAttribute("appointmentForm", form);
            mav.getModelMap().addAttribute("error", error);
            mav.getModelMap().addAttribute("doctors", instance.getUserRepository().getByRole(UserRole.DOCTOR));

            mav.setViewName("AppointmentEditor");
        }
        else {
            Appointment appointment = new Appointment();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            appointment.setAppointmentDate(simpleDateFormat.parse(form.getAppointmentDate()));

            appointment.setState(AppointmentState.NOT_CONSISTENT);
            appointment.setDoctorId(form.getDoctorId());
            appointment.setPatientId(instance.getCurrentUser().getId());

            instance.getAppointmentRepository().insert(appointment);

            mav.setViewName("redirect:/app/MainPage");
        }

        return mav;
    }

    @GetMapping({ "/app/UserAppointmentViewer" })
    public ModelAndView userAppointmentsView(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("AppointmentViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<AppointmentViewForm> viewForms = new ArrayList<>();

        User patient = instance.getCurrentUser();

        for (Appointment i : instance.getAppointmentRepository().getByUserId(patient.getId())) {
            AppointmentViewForm viewForm = new AppointmentViewForm();

            viewForm.setAppointment(i);
            viewForm.setPatient(patient);
            viewForm.setDoctor(instance.getUserRepository().getById(i.getDoctorId()));

            viewForms.add(viewForm);
        }

        mav.getModelMap().addAttribute("appointmentForms", viewForms);
        mav.getModelMap().addAttribute("title", "Текущие записи");

        mav.getModelMap().addAttribute("canEdit", true);
        mav.getModelMap().addAttribute("userRole", patient.getUserRole().getValue());

        return mav;
    }

    @GetMapping( { "/app/DoctorAppointmentViewer_All" } )
    public ModelAndView doctorAllAppointments(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("AppointmentViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<AppointmentViewForm> viewForms = new ArrayList<>();

        User doctor = instance.getCurrentUser();

        for (Appointment i : instance.getAppointmentRepository().getByUserId(doctor.getId())) {
            AppointmentViewForm viewForm = new AppointmentViewForm();

            viewForm.setAppointment(i);
            viewForm.setPatient(instance.getUserRepository().getById(i.getPatientId()));
            viewForm.setDoctor(doctor);

            viewForms.add(viewForm);
        }

        mav.getModelMap().addAttribute("appointmentForms", viewForms);
        mav.getModelMap().addAttribute("title", "Все записи");

        mav.getModelMap().addAttribute("canEdit", false);
        mav.getModelMap().addAttribute("userRole", doctor.getUserRole().getValue());

        return mav;
    }

    @GetMapping( { "/app/DoctorAppointmentViewer_Actual" } )
    public ModelAndView doctorActualAppointments(HttpServletRequest request) throws SQLException {
        ModelAndView mav = new ModelAndView("AppointmentViewer");

        SessionInstance instance = SessionManager.getSession(request.getSession().getId());

        assert instance != null;

        List<AppointmentViewForm> viewForms = new ArrayList<>();

        User doctor = instance.getCurrentUser();

        for (Appointment i : instance.getAppointmentRepository().getByUserId(doctor.getId())) {
            AppointmentViewForm viewForm = new AppointmentViewForm();

            viewForm.setAppointment(i);
            viewForm.setPatient(instance.getUserRepository().getById(i.getPatientId()));
            viewForm.setDoctor(doctor);

            if (viewForm.getAppointment().getState() == AppointmentState.NOT_CONSISTENT ||
                    viewForm.getAppointment().getState() == AppointmentState.CONSISTENT) {

                viewForms.add(viewForm);
            }
        }

        mav.getModelMap().addAttribute("appointmentForms", viewForms);
        mav.getModelMap().addAttribute("title", "Актуальные записи");

        mav.getModelMap().addAttribute("canEdit", true);
        mav.getModelMap().addAttribute("userRole", doctor.getUserRole().getValue());

        return mav;
    }

    @PutMapping({ "/app/CancelAppointment" })
    @Async
    public void cancelAppointment(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestBody AppointmentUpdateForm form) throws IOException {

        response.setContentType("text/plain");
        try {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            Appointment appointment = instance.getAppointmentRepository().getById(form.getAppointmentId());

            switch (UserRole.getByValue(form.getUserRole())) {
                case DOCTOR -> appointment.setState(AppointmentState.REJECTED_DOCTOR);
                case PATIENT -> appointment.setState(AppointmentState.REJECTED_PATIENT);
            }

            instance.getAppointmentRepository().update(appointment);

            response.getWriter().write("Ok");
        }
        catch (SQLException e) {
            response.getWriter().write(e.getMessage());
        }
    }

    @PutMapping({ "/app/UpdateAppointment" })
    @Async
    public void updateAppointment(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestBody AppointmentUpdateForm form) throws IOException {

        response.setContentType("text/plain");
        try {
            SessionInstance instance = SessionManager.getSession(request.getSession().getId());

            assert instance != null;

            Appointment appointment = instance.getAppointmentRepository().getById(form.getAppointmentId());

            switch (appointment.getState()) {
                case NOT_CONSISTENT -> appointment.setState(AppointmentState.CONSISTENT);
                case CONSISTENT -> appointment.setState(AppointmentState.VISITED);
            }

            instance.getAppointmentRepository().update(appointment);

            instance.getAppointmentRepository().update(appointment);

            response.getWriter().write("Ok");
        }
        catch (SQLException e) {
            response.getWriter().write(e.getMessage());
        }
    }
}