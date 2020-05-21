package kea.motorhome.motorhomesite.controllers;


import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.enums.ReservationStatus;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

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

    @PostMapping("reservation/lookup") //
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

        return "reservation/mockup";
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

        // skal lige have bygget en fail-safe her

        Reservation reservation = // nb. some fields are yet unknown
                new Reservation().
                        setStatus(ReservationStatus.Initialized).
                        setReservationID(pseudoID++).
                        setCustomer(customer).
                        setMotorhome(motorhome).
                        setPeriod(new Period(dateA.toLocalDate(), dateB.toLocalDate())).
                        setEmployee(dao.employeeDAO().read(1)).
                        setInternalNotes("Reservation was placed on:" + LocalDate.now());

        System.out.println(reservation);

        model.addAttribute("reservation", reservation);
        model.addAttribute("priceCalculator", new PriceCalculator());

        dao.reservationDAO().create(reservation);

        return "reservation/new";
    }


    @PostMapping("reservation/confirm")
    public String addReservationToSystem(@RequestParam int reservationID,
                                         @RequestParam String notes,
                                         @RequestParam int employeeID,
                                         Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        reservation.setStatus(ReservationStatus.Accepted);

        Employee employee = dao.employeeDAO().read(employeeID);

        if(employee != null)
        {
            reservation.setEmployee(employee);
        } else
        { // in case of bad employee ID, Employee ID 1 is used
            employee = dao.employeeDAO().read(1);
            reservation.setEmployee(employee);
        }

        if(notes != null){ reservation.setNotes(notes);}

        System.out.println(reservation);

        dao.reservationDAO().update(reservation);

        model.addAttribute("reservation", reservation);

        return "reservation/confirm";
    }


    @PostMapping("reservation/addservice")
    public String addServiceToReservation(@RequestParam int reservationID,
                                          @RequestParam int serviceID,
                                          Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        Service service = dao.serviceDAO().read(serviceID); //

        reservation.getServices().add(service); // multiple copies of the same service is possible on 1 res.

        model.addAttribute("reservation", reservation);
        model.addAttribute("priceCalculator", new PriceCalculator());

        return "reservation/new";
    }

    @PostMapping("reservation/removeservice")
    public String removeServicefromReservation(@RequestParam int reservationID,
                                               @RequestParam int serviceID,
                                               Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        for(int i = 0; i < reservation.getServices().size(); i++)
        {
            if(reservation.getServices().get(i).getServiceID() == serviceID)
            {
                reservation.getServices().remove(i);
                break;
            }
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("priceCalculator", new PriceCalculator());

        return "reservation/new";
    }

    @GetMapping("reservation/list")
    public String listReservationOptions(Model model)
    {
        model.addAttribute("reservations", dao.reservationDAO().readall());

        return "reservation/list";
    }

    @PostMapping("reservation/list-res-id")
    public String searchReservationsForID(@RequestParam int reservationID,
                                          Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao.reservationDAO().readall())
        {
            if(reservation.getReservationID() == reservationID)
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);

        return "reservation/list";
    }

}
