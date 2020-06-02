package kea.motorhome.motorhomesite.controllers;
//  kcn

import kea.motorhome.motorhomesite.dao.PeriodDAO;
import kea.motorhome.motorhomesite.mail.PreparedOutGoingMessage;
import kea.motorhome.motorhomesite.mail.SimpleMessageSender;
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
import java.util.List;

@Controller // annotation marking class as controller class
public class EmployeeReservationController
{
//    private SiteDAOCollection dao; // all DAOs in one

    public EmployeeReservationController()
    {
        /*dao = SiteDAOCollection.getInstance();*/
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("/reservation/")
    public String employeeMakeReservation(Model model)
    {
        addControllerStandardAttributes(model);
        return "reservation/landing";
    }

    private Model addControllerStandardAttributes(Model model)
    {
        model.addAttribute("dateUtil", new DateUtil());
        model.addAttribute("priceCalculator", new PriceCalculator());

        return model;
    }

    /**
     * Method interprets a desired period of rent of a motorhome:
     * Returns different views depending on circumstance.
     * If customer or vehicle do not exist, show error-page, ditto for already booked
     */
    @PostMapping("reservation/mockup") //
    public String lookupCustomerAndVehicle(@RequestParam String customerID,
                                           @RequestParam Integer motorhomeID,
                                           @RequestParam java.sql.Date dateA,
                                           @RequestParam java.sql.Date dateB,
                                           Model model)
    {
        Motorhome motorhome = dao().motorhomeDAO().read(motorhomeID);
        Customer customer = dao().customerDAO().read(customerID);

        /* if the motorhome and customer exist */
        if(motorhome != null && customer != null)
        {   /* looking through reservations */
            for(Reservation reservation : dao().reservationDAO().readall())
            {   /* checking if motorhome already reserved in period */
                if(reservation.getMotorhome().getMotorhomeID() == motorhomeID)
                {
                    Period desiredPeriod = new Period(dateA.toLocalDate(), dateB.toLocalDate());
                    if(reservation.getPeriod().overlapsWith(desiredPeriod))
                    { /* Show the error page pointing out that vehicle is already reserved in period */
                        return showErrorPage("Motorhome (" + motorhomeID + ")  already booked in the period " +
                                             "starting " + desiredPeriod.getStart() + " to " + desiredPeriod.getEnd() +
                                             ". Try again! :D", model);
                    }
                }
            }
            /* else, show mockup of reservation */
            model.addAttribute("motorhome", motorhome);
            model.addAttribute("customer", customer);
            model.addAttribute("periodAB", new Period(dateA.toLocalDate(), dateB.toLocalDate()));
            model.addAttribute("dateA", dateA);
            model.addAttribute("dateB", dateB);

            addControllerStandardAttributes(model);

            return "reservation/mockup";
        }

        return showErrorPage("Error: Could not find a) customer or b) motorhome.", model);
    }

    private String showErrorPage(@RequestParam String errorMessage, Model model)
    {
        model.addAttribute("errorMessage", errorMessage);
        return "reservation/error";
    }

    /**
     * Controller returns a new reservation object,
     * based on the customer and motorhome id supplied.
     * Reservation is created in system as Initialized.
     */
    @PostMapping("reservation/new")
    public String newReservation(@RequestParam String customerID,
                                 @RequestParam Integer motorhomeID,
                                 @RequestParam java.sql.Date dateA,
                                 @RequestParam java.sql.Date dateB,
                                 Model model)
    {
        Customer customer = dao().customerDAO().read(customerID);
        Motorhome motorhome = dao().motorhomeDAO().read(motorhomeID);

        if(customer != null && motorhome != null)
        {
            // specific DAO because of pattern breaking method create_getID
            PeriodDAO periodDAO = new PeriodDAO();

            Period period = new Period(dateA.toLocalDate(), dateB.toLocalDate());
            int periodID = periodDAO.create_getID(period);
            period = dao().periodDAO().read(periodID); // this jump ensures the auto-generated ID is set
            if(period==null) {
                return showErrorPage("An error happened, could not create reservation" +
                                     "period. Report to support.",model);
            }

            Reservation reservation = // nb. some fields are yet unknown
                    new Reservation().
                            setStatus(ReservationStatus.Initialized).
                            setReservationID(dao().reservationDAO().readall().size() + 1).
                            setCustomer(customer).
                            setMotorhome(motorhome).
                            setPeriod(period).
                            setEmployee(dao().employeeDAO().read(1)).
                            setInternalNotes("Reservation was placed on: " + LocalDate.now());

            model.addAttribute("reservation", reservation);
            model.addAttribute("priceCalculator", new PriceCalculator());

            dao().reservationDAO().create(reservation);

            return "reservation/new";
        }

        return showErrorPage("An error happened: Either customer with driver's licence " + customerID
                             + " does not exist in the system, or motorhome with id " + motorhomeID +
                             "does not exist. Could not load.", model);
    }

    /**
     * Method
     * 1) updates new reservation to Accepted, updates DB via DAO.
     * 2) triggers sending of confirmation email
     * 3) returns a user-friendly confirmation of the above in 'confirm' view.
     */
    @PostMapping("reservation/confirm")
    public String addReservationToSystem(@RequestParam int reservationID,
                                         @RequestParam String notes,
                                         @RequestParam int employeeID,
                                         Model model)
    {
        Reservation reservation = dao().reservationDAO().read(reservationID);

        if(reservation != null)
        {
            reservation.setStatus(ReservationStatus.Accepted);

            Employee employee = dao().employeeDAO().read(employeeID);

            if(employee != null)
            {
                reservation.setEmployee(employee);
            } else
            { // in case of bad employee ID, Employee ID 1 is used
                employee = dao().employeeDAO().read(1);
                reservation.setEmployee(employee);
            }

            if(notes != null){ reservation.setNotes(notes);} else{reservation.setNotes("No notes.");}


            model.addAttribute("reservation", reservation);

            /* send reservation confirmation-mail to customer */
            sendConfirmationEmailToCustomer(reservation);

            return "reservation/confirm";
        }

        //        in null cases
        return showErrorPage("This is odd. No reservation with id " + reservationID + " was found", model);

    }

    /* Method sends a confirmation email to the customer. */
    private void sendConfirmationEmailToCustomer(Reservation reservation)
    {
        PreparedOutGoingMessage prepared = new PreparedOutGoingMessage();
        String message = prepared.getReservationConfirmationEmailText(reservation);

        SimpleMessageSender sender = SimpleMessageSender.motorhomeStandardConnection();

        sender.sendEmail(reservation.getCustomer().getPerson().getEmail(),
                         "Congrats on your Reservation (" + reservation.getReservationID() + ")", message);
    }

    @PostMapping("reservation/addservice")
    public String addServiceToReservation(@RequestParam int reservationID,
                                          @RequestParam int serviceID,
                                          @RequestParam String requestURL, // std: "reservation/new" / update
                                          Model model)
    {
        Reservation reservation = dao().reservationDAO().read(reservationID);
        Service service = dao().serviceDAO().read(serviceID);

        if(reservation != null && service != null)
        {
            reservation.getServices().add(service); // multiple copies of the same service is possible on 1 res.

            dao().reservationDAO().update(reservation); // reservation is updated

            model.addAttribute("reservation", reservation);
            addControllerStandardAttributes(model);

            return "reservation/" + requestURL;
        }
        //        in null cases
        return showErrorPage("No reservation with id " + reservationID
                             + " OR no service with id " + serviceID, model);
    }

    @PostMapping("reservation/removeservice")
    public String removeServicefromReservation(@RequestParam int reservationID,
                                               @RequestParam int serviceID,
                                               @RequestParam String requestURL,
                                               Model model)
    {
        Reservation reservation = dao().reservationDAO().read(reservationID);

        for(int i = 0; i < reservation.getServices().size(); i++)
        {
            if(reservation.getServices().get(i).getServiceID() == serviceID)
            {
                reservation.getServices().remove(i);
                break;
            }
        }

        dao().reservationDAO().update(reservation); // reservation is updated

        model.addAttribute("reservation", reservation);
        addControllerStandardAttributes(model);

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

        for(Reservation rsrvation : dao().reservationDAO().readall())
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
        addControllerStandardAttributes(model);

        Reservation reservation = dao().reservationDAO().read(reservationID);

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
        Reservation reservation = dao().reservationDAO().read(reservationID);
        if(reservation != null)
        {
            reservation.setStatus(ReservationStatus.status(reservationStatus));
            dao().reservationDAO().update(reservation);

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
        Reservation reservation = dao().reservationDAO().read(reservationID);
        reservation.getPeriod().setStart(dateA.toLocalDate());
        reservation.getPeriod().setEnd(dateB.toLocalDate());

        dao().reservationDAO().update(reservation);
        dao().periodDAO().update(reservation.getPeriod());

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
        Reservation reservation = dao().reservationDAO().read(reservationID);

        if(reservation != null)
        {
            reservation.setNotes(notes);
            reservation.setInternalNotes(internalNotes);
            dao().reservationDAO().update(reservation);

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
        boolean deleteWasASuccess = dao().reservationDAO().delete(reservationID);

        if(deleteWasASuccess)
        {
            model.addAttribute("reservations", dao().reservationDAO().readall());
            addControllerStandardAttributes(model);
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
        model.addAttribute("reservations", dao().reservationDAO().readall());
        addControllerStandardAttributes(model);

        return "reservation/list";
    }

    @PostMapping("/reservation/searchByReservationID")
    public String searchReservationsForID(@RequestParam int reservationID, Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao().reservationDAO().readall())
        {
            if(reservation.getReservationID() == reservationID)
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);
        addControllerStandardAttributes(model);

        return "reservation/list";
    }

    /**
     * Returns a view with a list of reservations matching queried employeeID
     */
    @PostMapping("/reservation/searchByEmployeeID")
    public String searchByEmployeeID(@RequestParam int employeeID, Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao().reservationDAO().readall())
        {
            if(reservation.getEmployee().getEmployeeID() == employeeID)
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);

        addControllerStandardAttributes(model);

        return "reservation/list";
    }

    /**
     * Returns a view with a list of reservations matching queries driver's licence / customer id
     */
    @PostMapping("/reservation/searchByCustomerID")
    public String searchByCustomerID(@RequestParam String driversLicence, Model model)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();

        for(Reservation reservation : dao().reservationDAO().readall())
        {
            if(reservation.getCustomer().getDriversLicence().toLowerCase().contentEquals(driversLicence.toLowerCase()))
            {
                reservations.add(reservation);
            }
        }

        model.addAttribute("reservations", reservations);

        addControllerStandardAttributes(model);

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
        /* list where search result positives show up */
        ArrayList<Reservation> reservations = new ArrayList<>();
        // local copy to avoid multiple calls to database
        List<Reservation> inDatabaseReservations = dao().reservationDAO().readall();

        Period period = new Period(dateA.toLocalDate(), dateB.toLocalDate());
        /* if dateA/dateB overlaps with period start or end, include in search */
        if(resultCriteria.contentEquals("Start Date"))
        {
            for(Reservation r : inDatabaseReservations)
            {
                if(period.dateOverlapsWithPeriod(r.getPeriod().getStart())){ reservations.add(r);}
            }
        } else
        {
            for(Reservation r : inDatabaseReservations)
            {
                if(period.dateOverlapsWithPeriod(r.getPeriod().getEnd())){ reservations.add(r);}
            }
        }

        model.addAttribute("reservations", reservations);

        addControllerStandardAttributes(model);

        return "reservation/list";
    }
}
