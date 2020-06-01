package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MotorhomeController {

//    private SiteDAOCollection dao;

    public MotorhomeController() {
        /*dao = SiteDAOCollection.getInstance();*/
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("/motorhomes/motorhomes")
    public String motorhomesPage(Model model) {
        model.addAttribute("motorhomes", dao().motorhomeDAO().readall());
        return "motorhomes/motorhomes";
    }

    @GetMapping("/motorhomes/details")
    public String getMotorhomeByParameter(Model model, @RequestParam int id) {
        Motorhome motorhome = dao().motorhomeDAO().read(id);
        model.addAttribute("motorhome", motorhome);
        model.addAttribute("services", motorhome.getServicesAvailable());
        return "motorhomes/details";
    }

    @GetMapping("/motorhomes/new")
    public String showNewMotorhomeForm(Model model) {
        Motorhome motorhome = new Motorhome();
        motorhome.setMotorhomeID(dao().motorhomeDAO().readall().size() + 1);
        motorhome.setSeasonalDailyCharge(new float[3]);
        model.addAttribute("motorhome", motorhome);
        model.addAttribute("services", dao().serviceDAO().readall());
        model.addAttribute("carModels", dao().carModelDAO().readall());
        return "motorhomes/new";
    }

    @RequestMapping(value = "/savemotorhome", method = RequestMethod.POST)
    public String createMotorhome(@ModelAttribute("motorhome") Motorhome motorhome, @RequestParam int carModelID) {
        CarModel carModel = dao().carModelDAO().read(carModelID);
        motorhome.setModel(carModel);
        dao().motorhomeDAO().create(motorhome);
        return "redirect:/motorhomes/motorhomes";
    }

    @RequestMapping(value ="/updatemotorhome", method = RequestMethod.POST)
    public String updateMotorhome(@ModelAttribute("motorhome") Motorhome motorhome, @RequestParam int carModelID) {
        CarModel carModel = dao().carModelDAO().read(carModelID);
        motorhome.setModel(carModel);
        dao().motorhomeDAO().update(motorhome);
        return "redirect:/motorhomes/motorhomes";
    }

    @GetMapping("/motorhomes/edit")
    public String showEditMotorhomeForm(Model model, @RequestParam int id) {
        model.addAttribute("motorhome", dao().motorhomeDAO().read(id));
        model.addAttribute("services", dao().serviceDAO().readall());
        model.addAttribute("carModels",dao().carModelDAO().readall());
        return "motorhomes/edit";
    }

    @RequestMapping("motorhomes/delete")
    public String deleteMotorhome(@RequestParam int id) {
        dao().motorhomeDAO().delete(id);
        return "redirect:/motorhomes/motorhomes";
    }


}