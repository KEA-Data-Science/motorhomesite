package kea.motorhome.motorhomesite.controllers;
// by LNS
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarModelController {

//    private SiteDAOCollection dao;

    public CarModelController() {
//        dao = SiteDAOCollection.getInstance();
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("/carmodels/carmodels")
    public String carModelsPage(Model model) {
        model.addAttribute("carModels", dao().carModelDAO().readall());
        return "carmodels/carmodels";
    }

    @GetMapping("/carmodels/details")
    public String getCarModelByParameter(Model model, @RequestParam int id) {
        model.addAttribute("carModel", dao().carModelDAO().read(id));
        return "carmodels/details";
    }

    @GetMapping("/carmodels/new")
    public String showNewCarModelForm(Model model) {
        CarModel carModel = new CarModel();
        carModel.setCarModelID(dao().carModelDAO().readall().size() + 1);
        model.addAttribute("carModel", carModel);
        return "carmodels/new";
    }

    @RequestMapping(value = "/savecarmodel", method = RequestMethod.POST)
    public String createCarModel(@ModelAttribute("carModel") CarModel carModel) {
        dao().carModelDAO().create(carModel);
        return "redirect:/carmodels/carmodels";
    }

    @RequestMapping(value ="/updatecarmodel", method = RequestMethod.POST)
    public String updateCarModel(@ModelAttribute("carmodel") CarModel carModel) {
        dao().carModelDAO().update(carModel);
        return "redirect:/carmodels/carmodels";
    }

    @GetMapping("/carmodels/edit")
    public String showEditCarModelForm(Model model, @RequestParam int id) {
        model.addAttribute("carModel", dao().carModelDAO().read(id));
        return "carmodels/edit";
    }

    @RequestMapping("carmodels/delete")
    public String deleteCarModel(@RequestParam int id) {
        dao().carModelDAO().delete(id);
        return "redirect:/carmodels/carmodels";
    }


}