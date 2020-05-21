package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.daodemo.ServiceDAODemo;
import kea.motorhome.motorhomesite.models.Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServiceController {

    ServiceDAODemo serviceDAODemo;

    public ServiceController()
    {
        serviceDAODemo = new ServiceDAODemo();
//        serviceDAODemo.addServices();

    }
    @GetMapping("/services/services")
    public String showServices(Model model)
    {
        model.addAttribute("services", serviceDAODemo.readall());
        return "/services/services";
    }

    @GetMapping("/services/showcreateservice")
    public String createService()
    {
        return "/services/createservice";
    }

    @PostMapping("/services/getdata")
    public String getData(@ModelAttribute Service service)
    {
        service.setServiceID(serviceDAODemo.readall().size());
        serviceDAODemo.create(service);
        return "redirect:/services/services";
    }

    @GetMapping("/services/showupdateservice")
    public String showUpdateService(@RequestParam int id, Model model)
    {
        model.addAttribute("service", serviceDAODemo.read(id));
        return "/services/updateservice";
    }

    @PostMapping("/services/performUpdate")
    public String performUpdate(@ModelAttribute Service service)
    {
        serviceDAODemo.update(service);
        System.out.println(service);
        return "redirect:/services/services";
    }

    @GetMapping("/services/deleteservice")
    public String deleteService(@RequestParam int id)
    {
        serviceDAODemo.delete(id);
        return "redirect:/services/services";
    }
}
