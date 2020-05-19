package kea.motorhome.motorhomesite.controllers;


import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.CustomerDAODemo;
import kea.motorhome.motorhomesite.daodemo.MotorhomeDAODemo;
import kea.motorhome.motorhomesite.daodemo.ReservationDAODemo;
import kea.motorhome.motorhomesite.models.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // annotation
public class EmployeeReservationController
{
    private IDAO<Reservation, Integer> reservationDAO;

    public EmployeeReservationController()
    {
        reservationDAO = new ReservationDAODemo(); // notice demo DAO
    }

    @GetMapping("/reservation/")
    public String employeeMakeReservation()
    {
        return "reservation/landing";
    }

    @PostMapping("/lookup") //
    public String lookupCustomerAndVehicle(@RequestParam String customerID,
                                           @RequestParam Integer motorhomeID,
                                           @RequestParam java.sql.Date dateA,
                                           @RequestParam java.sql.Date dateB,
                                           Model model
                                          )
    {
        model.addAttribute("motorhome", new MotorhomeDAODemo().read(motorhomeID));
        model.addAttribute("customer",new CustomerDAODemo().read(customerID));

        model.addAttribute("dateA",dateA);
        model.addAttribute("dateB",dateB);

        return "reservation/lookup";
    }

    @GetMapping("/reservation/read")
    @ResponseBody
    public String readReservation(@RequestParam String id)
    {
        return "You followed /reservation/read " + id;

    }

    @GetMapping("reservation/new")
    public String newReservation(@RequestParam String customerID,
                                 @RequestParam Integer motorhomeID,
                                 @RequestParam java.sql.Date dateA,
                                 @RequestParam java.sql.Date dateB,
                                 Model model){
        model.addAttribute("motorhome", new MotorhomeDAODemo().read(motorhomeID));
        model.addAttribute("customer",new CustomerDAODemo().read(customerID));

        model.addAttribute("dateA",dateA);
        model.addAttribute("dateB",dateB);

        return "reservation/new";
    }

}
