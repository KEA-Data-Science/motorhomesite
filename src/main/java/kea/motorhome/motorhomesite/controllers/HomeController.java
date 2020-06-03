package kea.motorhome.motorhomesite.controllers;
// kcn

import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.Motorhome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController
{

    public HomeController(){ }

    @GetMapping("/")
    public String home(){ return "index"; }

    @GetMapping("/gallery")
    public String galleryPage(Model model)
    {
        model.addAttribute("mobileHomes", dao().motorhomeDAO().readall());

        return "gallery";
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @PostMapping("/feedback")
    public String showFeedback(@RequestParam String feedbackMessage, Model model)
    {
        if(feedbackMessage != null && feedbackMessage.length() > 0)
        {
            model.addAttribute("feedbackMessage", feedbackMessage);
        } else
        {
            model.addAttribute("feedbackMessage", "You were redirected to here; but there is " +
                                                  "no message to show");
        }

        return "feedback";
    }

}
