package kea.motorhome.motorhomesite.controllers;
//  kcn
import kea.motorhome.motorhomesite.util.DateUtil;
import kea.motorhome.motorhomesite.util.PriceCalculator;
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
        pseudoID = 1;// demo variable
        dao = SiteDAOCollection.getInstance();
    }

    @GetMapping("/reservation/")
    public String employeeMakeReservation(Model model)
    {
        addStandardAttributes(model);
        return "reservation/landing";
    }

    private Model addStandardAttributes(Model model)
    {
        model.addAttribute("dateUtil", new DateUtil());
        model.addAttribute("priceCalculator", new PriceCalculator());

        return model;
    }

    @PostMapping("reservation/mockup") //
    public String lookupCustomerAndVehicle(@RequestParam String customerID,
                                           @RequestParam Integer motorhomeID,
                                           @RequestParam java.sql.Date dateA,
                                           @RequestParam java.sql.Date dateB,
                                           Model model
                                          )
    {
        Motorhome motorhome = dao.motorhomeDAO().read(motorhomeID);
        Customer customer = dao.customerDAO().read(customerID);

        if(motorhome != null && customer != null)
        {
            model.addAttribute("motorhome", motorhome);
            model.addAttribute("customer", customer);
            model.addAttribute("dateA", dateA);
            model.addAttribute("dateB", dateB);

            return "reservation/mockup";
        }

        return showErrorPage("Error: Could not find a) customer or b) motorhome.", model);
    }

//    @GetMapping("/reservation/read")
//    @ResponseBody
//    public String readReservation(@RequestParam String id)
//    {
//        return "You followed /reservation/read " + id;
//    }

    private String showErrorPage(@RequestParam String errorMessage, Model model)
    {
        model.addAttribute("errorMessage", errorMessage);
        return "reservation/error";
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
                        setInternalNotes("Reservation was placed on: " + LocalDate.now());

//        System.out.println(reservation);

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

        if(reservation != null)
        {

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

            if(notes != null){ reservation.setNotes(notes);} else{reservation.setNotes("No notes.");}


            model.addAttribute("reservation", reservation);

            return "reservation/confirm";

        }

        //        in null cases
        return showErrorPage("This is odd. No reservation with id " + reservationID + " was found", model);

    }

    @PostMapping("reservation/addservice")
    public String addServiceToReservation(@RequestParam int reservationID,
                                          @RequestParam int serviceID,
                                          @RequestParam String requestURL, // std: "reservation/new" / update
                                          Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        Service service = dao.serviceDAO().read(serviceID); //

        if(reservation != null && service != null)
        {
            reservation.getServices().add(service); // multiple copies of the same service is possible on 1 res.

            dao.reservationDAO().update(reservation); // reservation is updated

            model.addAttribute("reservation", reservation);
            addStandardAttributes(model);

            return "reservation/" + requestURL;
        }
        //        in null cases
        return showErrorPage("No reservation with id " + reservationID + " OR no service with id " + serviceID, model);
    }

    @PostMapping("reservation/removeservice")
    public String removeServicefromReservation(@RequestParam int reservationID,
                                               @RequestParam int serviceID,
                                               @RequestParam String requestURL,
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

        dao.reservationDAO().update(reservation); // reservation is updated

        model.addAttribute("reservation", reservation);
        addStandardAttributes(model);

        return "reservation/" + requestURL;
    }

    /**
     * Returns a view with a Reservation object found by ID: for viewing details
     */
    @PostMapping("reservation/details")
    public String detailsReservation(@RequestParam int reservationID, Model model)
    {
        /* Making sure that reservation is added to model, even if null */
        Reservation reservation = null;

        for(Reservation rsrvation : dao.reservationDAO().readall())
        {
            if(rsrvation.getReservationID() == reservationID)
            {
                reservation = rsrvation;
                break;
            }
        }

        if(reservation != null)
        {
            model.addAttribute("reservation", reservation);
            model.addAttribute("reservationID", reservationID);

            return "reservation/details";
        }

        //        in null cases
        return showErrorPage("Details could not display; no reservation found", model);
    }

    /**
     * Returns the error view primed with a message in the model
     */
    @PostMapping("reservation/error")
    public String reservationRequestError(@RequestParam String errorMessage,
                                          Model model)
    {
        model.addAttribute("errorMessage", errorMessage);
        return "reservation/error";
    }

    /**
     * Returns a view with a Reservation object found by ID: for update
     */
    @PostMapping("/reservation/update")
    public String updateReservation(@RequestParam int reservationID,
                                    Model model)
    {
        addStandardAttributes(model);

        Reservation reservation = dao.reservationDAO().read(reservationID);

        if(reservation != null)
        {
            model.addAttribute("reservation", reservation);
            model.addAttribute("reservationID", reservationID);


            return "reservation/update";
        }

        //   in null cases
        return showErrorPage("Reservation ID not recognized", model);
    }

    /**
     * Method udates reservation update status (en'DAO'ed) and returns the 'details view' for the
     * reservation
     */
    @PostMapping("/reservation/updateStatus")
    public String updateReservationStatus(@RequestParam String reservationStatus,
                                          @RequestParam int reservationID,
                                          Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);
        if(reservation != null)
        {
            reservation.setStatus(ReservationStatus.status(reservationStatus));
            dao.reservationDAO().update(reservation);

            model.addAttribute("reservation", reservation);
            model.addAttribute("reservationID", reservation.getReservationID());
            return "reservation/update";
        }
        // in null cases
        return showErrorPage("Reservation status could not be updated", model);
    }

    @PostMapping("/reservation/confirmPeriodUpdate")
    public String updateReservationPeriod(@RequestParam int reservationID,
                                          @RequestParam java.sql.Date dateA,
                                          @RequestParam java.sql.Date dateB,
                                          Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);
        reservation.getPeriod().setStart(dateA.toLocalDate());
        reservation.getPeriod().setEnd(dateB.toLocalDate());

        dao.reservationDAO().update(reservation);

        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationID", reservation.getReservationID());
        return "reservation/update";
    }

    @PostMapping("/reservation/confirmNotesUpdate")
    public String updateReservationNotes(@RequestParam int reservationID,
                                         @RequestParam String notes,
                                         @RequestParam String internalNotes,
                                         Model model)
    {
        Reservation reservation = dao.reservationDAO().read(reservationID);

        if(reservation != null)
        {
            reservation.setNotes(notes);
            reservation.setInternalNotes(internalNotes);
            dao.reservationDAO().update(reservation);

            model.addAttribute("reservation", reservation);
            model.addAttribute("reservationID", reservationID);

            return "reservation/update";
        }

        //in case something is wrong with the reservation-object; go to overview
        return showErrorPage("Reservation not Found", model);
    }

    /**
     * Erases s a reservation from the system, and returns to the list view of reservations
     */
    @PostMapping("reservation/delete")
    public String deleteReservation(@RequestParam int reservationID,
                                    Model model)
    {
        boolean deleteWasASuccess = dao.reservationDAO().delete(reservationID);

        if(deleteWasASuccess)
        {
            model.addAttribute("reservations", dao.reservationDAO().readall());
            addStandardAttributes(model);
            return "/reservation/list";
        }

        return showErrorPage("Error. Could not delete reservation with id " + reservationID + "from system"
                , model);
    }


    /**
     * Returns a view with search option and list
     * Main reservation listing page
     */
    @GetMapping("reservation/list")
    public String listReservationOptions(Model model)
    {
        model.addAttribute("reservations", dao.reservationDAO().readall());
        addStandardAttributes(model);

        return "reservation/list";
    }

    @PostMapping("/reservation/searchByReservationID")
    public String searchReservationsForID(@RequestParam int reservationID, Model model)
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
        addStandardAttributes(model);

        return "reservation/list";
    }

    /**
     * Returns a view with a list of reservations matching queried employeeID
     */
    @PostMapping("/reservation/searchByEmployeeID")
    public String searchByEmployeeID(@RequestParam int employeeID, Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao.reservationDAO().readall())
        {
            if(reservation.getEmployee().getEmployeeID() == employeeID)
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);

        addStandardAttributes(model);

        return "reservation/list";
    }

    /**
     * Returns a view with a list of reservations matching queries driver's licence / customer id
     */
    @PostMapping("/reservation/searchByCustomerID")
    public String searchByCustomerID(@RequestParam String driversLicence, Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao.reservationDAO().readall())
        {
            if(reservation.getCustomer().getDriversLicence().toLowerCase().contentEquals(driversLicence.toLowerCase()))
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);

        addStandardAttributes(model);

        return "reservation/list";
    }

    /**
     * Returns either all reservations that have a start date within giver period - or end date ditto.
     */
    @PostMapping("/reservation/searchByPeriod")
    public String searchByPeriod(@RequestParam java.sql.Date dateA,
                                 @RequestParam java.sql.Date dateB,
                                 String resultCriteria, // the query-topic decides if start,end or whole
                                 // periods
                                 Model model)
    {

        ArrayList<Reservation> reservations = new ArrayList<>();

        Period period = new Period(dateA.toLocalDate(), dateB.toLocalDate());

        if(resultCriteria.contentEquals("Start Date"))
        {
            for(Reservation r : dao.reservationDAO().readall())
            {
                if(period.dateOverlapsWithPeriod(r.getPeriod().getStart())){ reservations.add(r);}
            }
        } else
        {
            for(Reservation r : dao.reservationDAO().readall())
            {
                if(period.dateOverlapsWithPeriod(r.getPeriod().getEnd())){ reservations.add(r);}
            }
        }

        model.addAttribute("reservations", reservations);

        addStandardAttributes(model);

        return "reservation/list";
    }


}
