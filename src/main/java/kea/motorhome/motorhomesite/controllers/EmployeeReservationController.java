package kea.motorhome.motorhomesite.controllers;


import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.CustomerDAODemo;
import kea.motorhome.motorhomesite.daodemo.MotorhomeDAODemo;
import kea.motorhome.motorhomesite.daodemo.ReservationDAODemo;
import kea.motorhome.motorhomesite.models.Customer;
import kea.motorhome.motorhomesite.models.Motorhome;
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.models.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller // annotation
public class EmployeeReservationController
{
    private int pseudoID;
    private IDAO<Reservation, Integer> reservationDAO;

    public EmployeeReservationController()
    {
        // demo variable
        pseudoID = 1;

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
        model.addAttribute("customer", new CustomerDAODemo().read(customerID));

        model.addAttribute("dateA", dateA);
        model.addAttribute("dateB", dateB);

        return "reservation/lookup";
    }

    @GetMapping("/reservation/read")
    @ResponseBody
    public String readReservation(@RequestParam String id)
    {
        return "You followed /reservation/read " + id;
    }
    /**
     * Controller returns a new reservation object,
     * based on the customer and motorhome id supplied
     * wrapped in a list to maximize utility of new.html
     * */
    @PostMapping("reservation/new")
    public String newReservation(@RequestParam String customerID,
                                 @RequestParam Integer motorhomeID,
                                 @RequestParam java.sql.Date dateA,
                                 @RequestParam java.sql.Date dateB,
                                 Model model)
    {
        Customer customer = new CustomerDAODemo().read(customerID);
        Motorhome motorhome = new MotorhomeDAODemo().read(motorhomeID);

        Reservation reservation = // nb. some fields are yet unknown
                new Reservation().
                        setReservationID(pseudoID++).
                        setCustomer(customer).
                        setMotorhome(motorhome).
                        setPeriod(new Period(dateA.toLocalDate(), dateB.toLocalDate()));

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        model.addAttribute("reservations", reservations);

        model.addAttribute("priceCalculator",new PriceCalculator());

        return "reservation/new";
    }

}
