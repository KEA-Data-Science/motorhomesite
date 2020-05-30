package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppointmentController {

//    private SiteDAOCollection dao;

    public AppointmentController() {
//        dao = SiteDAOCollection.getInstance();
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("/appointments/appointments")
    public String appointmentsPage(Model model) {
        model.addAttribute("appointments", dao().appointmentDAO().readall());
        return "appointments/appointments";
    }

    @GetMapping("/appointments/details")
    public String getAppointmentByParameter(Model model, @RequestParam int id) {
        Appointment appointment = dao().appointmentDAO().read(id);
        model.addAttribute("motorhome", dao().motorhomeDAO().read(appointment.getMotorHomeID()));
        model.addAttribute("appointment", appointment);
        return "appointments/details";
    }

    @GetMapping("/appointments/new")
    public String showNewAppointmentForm(Model model) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentID(dao().appointmentDAO().readall().size() + 1);
        Address address = new Address();
        address.setAddressID(dao().addressDAO().readall().size() + 1);
        appointment.setAddress(address);
        List<Integer> employeeIDs = new ArrayList<>();
        appointment.setEmployeeIDs(employeeIDs);
        model.addAttribute("appointment", appointment);
        model.addAttribute("address", address);
        model.addAttribute("employeeIDs", employeeIDs);
        model.addAttribute("motorhomes", dao().motorhomeDAO().readall());
        model.addAttribute("employees", dao().employeeDAO().readall());
        return "appointments/new";
    }

    @RequestMapping(value = "/saveappointment", method = RequestMethod.POST)
    public String createAppointment(@ModelAttribute("appointment") Appointment appointment,
                                    @ModelAttribute("address") Address address) {
        dao().addressDAO().create(address);
        dao().appointmentDAO().create(appointment);
        return "redirect:/appointments/appointments";
    }

    @RequestMapping(value ="/updateappointment", method = RequestMethod.POST)
    public String updateAppointment(@ModelAttribute("appointment") Appointment appointment) {
        dao().appointmentDAO().update(appointment);
        return "redirect:/appointments/appointments";
    }

    @GetMapping("/appointments/edit")
    public String showEditAppointmentsForm(Model model, @RequestParam int id) {
        List<Motorhome> motorhomes = dao().motorhomeDAO().readall();
        model.addAttribute("appointment", dao().appointmentDAO().read(id));
        model.addAttribute("motorhomes", motorhomes);
        return "appointments/edit";
    }

    @RequestMapping("appointments/delete")
    public String deleteAppointment(@RequestParam int id) {
        dao().appointmentDAO().delete(id);
        return "redirect:/appointments/appointments";
    }
}