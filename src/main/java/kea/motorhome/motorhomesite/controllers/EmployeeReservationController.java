package kea.motorhome.motorhomesite.controllers;


import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller // annotation marking class as controller class
public class EmployeeReservationController
{
    private int pseudoID; // demo

    private SiteDAOCollection dao; // all DAOs in one

    public EmployeeReservationController()
    {
        // demo variable
        pseudoID = 1;
        dao = SiteDAOCollection.getInstance();
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
        model.addAttribute("motorhome", dao.motorhomeDAO().read(motorhomeID));
        model.addAttribute("customer", dao.customerDAO().read(customerID));

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
     */
    @PostMapping("reservation/new")
    public String newReservation(@RequestParam String customerID,
                                 @RequestParam Integer motorhomeID,
                                 @RequestParam java.sql.Date dateA,
                                 @RequestParam java.sql.Date dateB,
                                 Model model)
    {
        Customer customer = dao.customerDAO().read(customerID);
        Motorhome motorhome = dao.motorhomeDAO().read(motorhomeID);

        Reservation reservation = // nb. some fields are yet unknown
                new Reservation().
                        setReservationID(pseudoID++).
                        setCustomer(customer).
                        setMotorhome(motorhome).
                        setPeriod(new Period(dateA.toLocalDate(), dateB.toLocalDate()));

        /*   fordi html/thymeleaf kan håndtere flere   reservationer  */
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        model.addAttribute("reservations", reservations);
        model.addAttribute("priceCalculator", new PriceCalculator());

        dao.reservationDAO().create(reservation);

        return "reservation/new";
    }

    @PostMapping("reservation/addservice")
    public String addServiceToReservation(@RequestParam int reservationID,
                                          @RequestParam int serviceID,
                                          Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        Service service = dao.serviceDAO().read(serviceID);

        reservation.getServices().add(service); // multiple copies of the same service is possible on 1 res.

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        model.addAttribute("reservations", reservations);
        model.addAttribute("priceCalculator", new PriceCalculator());

        return "reservation/new";
    }

    @PostMapping("reservation/removeservice")
    public String removeServicefromReservation(@RequestParam int reservationID,
                                               @RequestParam int serviceID,
                                               Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        for(int i=0; i< reservation.getServices().size();i++)
        {
            if(reservation.getServices().get(i).getServiceID() == serviceID)
            {
                reservation.getServices().remove(i);
                break;
            }
        }

        List<Reservation> reservations = dao.reservationDAO().readall();

        model.addAttribute("reservations", reservations);
        model.addAttribute("priceCalculator", new PriceCalculator());

        return "reservation/new";
    }

}
