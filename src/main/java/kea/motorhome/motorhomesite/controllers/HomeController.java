package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.models1.MobileHomeModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController
{
    List<MobileHomeModel> mobileHomes;


    @GetMapping("/")
    public String home(){ return "index"; }

    @GetMapping("/drags")
    public String dragBoxes(){ return "draggablebox"; }

    @GetMapping("/boxing")
    public String boxingCSSExample(){ return "boxing"; }

    @GetMapping("/gallery")
    public String galleryPage(Model model)
    {
        mobileHomes = new ArrayList<>();
        mobileHomes.add(new MobileHomeModel(1,"Big One Runnrunn",2018
                ,"This wonderful vehicle can take you anywhere as long as it is not uphill.",
                                            203.0f,4,"/img/MH1.jpg"));
        mobileHomes.add(new MobileHomeModel(1,"Busty Deep Seater",2014
                ,"Thompsons Teeth are the Only Teeth that can Chew Other Teeth! Also, this is not a vehicle" +
                 ", but a prop useful only for pseudo-pornographic and semi-educational purposes.",
                                            203.0f,4,"../img/MH2.jpg"));
        mobileHomes.add(new MobileHomeModel(1,"Normal Mobile Home",2004
                ,"If you rent this car, nothing in your life will be the same. Literally, you will be " +
                 "transported to another Universe without oxygen, and sadly, suffocate. The good news is, " +
                 "you won't really be real at that point, because the laws of physics in that Universe do " +
                 "not allow matter to subsist.",
                                            203.0f,4,"img/MH3.jpg"));
        mobileHomes.add(new MobileHomeModel(1,"Heritage Mobile Home",1954
                ,"Winston Churchill spent his last four summers traveling around this old baby. She " +
                 "traveled the Alps before Tiger Woods. Anyway, the vehicle is a complete deathtrap.",
                                            203.0f,4,"../img/MH4.jpg"));

        model.addAttribute("mobileHomes",mobileHomes);
        return "gallery";
    }
}
