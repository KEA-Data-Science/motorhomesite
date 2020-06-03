package kea.motorhome.motorhomesite.controllers;
// by TV
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.ServiceDAODemo;
import kea.motorhome.motorhomesite.models.Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServiceController
{

    public ServiceController(){ }

    @GetMapping("services")
    public String showServices(Model model)
    {
        model.addAttribute("services", dao().serviceDAO().readall());
        return "services/services";
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("createservice")
    public String createService()
    {
        return "services/new";
    }

    @PostMapping("services/getdata")
    public String getData(@ModelAttribute Service service)
    {
        service.setServiceID(dao().serviceDAO().readall().size());
        dao().serviceDAO().create(service);
        return "redirect:services";
    }

    @GetMapping("updateservice")
    public String showUpdateService(@RequestParam int id, Model model)
    {
        model.addAttribute("service", dao().serviceDAO().read(id));
        return "services/edit";
    }

    @PostMapping("performUpdate")
    public String performUpdate(@ModelAttribute Service service)
    {
        dao().serviceDAO().update(service);
        System.out.println(service);
        return "redirect:services";
    }

    @GetMapping("deleteservice")
    public String deleteService(@RequestParam int id)
    {
        dao().serviceDAO().delete(id);
        return "redirect:services";
    }
}
