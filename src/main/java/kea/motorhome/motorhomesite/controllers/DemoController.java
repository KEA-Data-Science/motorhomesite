package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.Motorhome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DemoController
{
    List<Motorhome> mobileHomes;

    SiteDAOCollection dao; // all dao in one. dao is the way

    public DemoController()
    {
        dao = SiteDAOCollection.getInstance();
    }

    @GetMapping("/")
    public String home(){ return "index"; }


    @GetMapping("/gallery")
    public String galleryPage(Model model)
    {
        model.addAttribute("mobileHomes", dao.motorhomeDAO().readall());

        return "gallery";
    }

}
