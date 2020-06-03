package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.InvoiceDAO;
import kea.motorhome.motorhomesite.dao.PeriodDAO;
import kea.motorhome.motorhomesite.dao.PersonDAO;
import kea.motorhome.motorhomesite.mail.PreparedOutGoingMessage;
import kea.motorhome.motorhomesite.mail.SimpleMessageSender;
import kea.motorhome.motorhomesite.util.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InvoiceController
{

    public InvoiceController(){ }

    /*
        The method responsible for directing the user over to the invoices page where a list of all invoices is presented.
        It adds all of the invoices into the model and a price calculator which make it possible to calculate the price
        inside invoices.html.

     */
    @GetMapping("invoices")
    public String showInvoices(Model model)
    {
//        SiteDAOCollection dao = SiteDAOCollection.getInstance();
        PriceCalculator priceCalculator = new PriceCalculator();
        List<Invoice> invoices = new ArrayList<>(dao().invoiceDAO().readall());

        model.addAttribute("invoices", invoices);
        model.addAttribute("calculator", priceCalculator);

        return "invoices/invoices";
    }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    /*
        The method responsible for directing the user into the update page after they have chosen an
        invoice to update inside invoices.html. It gets the invoice id as a parameter, reads the invoice
        in the database via the invoice id, adds the invoice to model and show the update form.
     */

    @GetMapping("invoices/update")
    public String showUpdateForm(@RequestParam int id, Model model, HttpSession session)
    {
        session.setAttribute("invoiceUpdate", dao().invoiceDAO().read(id));
        addAttributesToModel(model, dao().invoiceDAO().read(id));
        return "invoices/edit";
    }

    /*
        The 4 attributes in this method need to be added to the model many times
        and the purpose of this method is to reduce redundancy.

     */
    public void addAttributesToModel(Model model, Invoice invoice)
    {
        model.addAttribute("invoice", invoice);
        model.addAttribute("motorhomes", dao().motorhomeDAO().readall());
        model.addAttribute("customers", dao().customerDAO().readall());
        model.addAttribute("services", dao().serviceDAO().readall());
    }

    /*
        This method is responsible for updating the invoice in the database. It is called after the user clicks submit
        inside invoices/edit.html.
     */

    @PostMapping("invoices/perfomupdate")
    public String performUpdate(WebRequest wr, Model model, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoiceUpdate");

        invoice.setInvoiceID(Integer.parseInt((wr.getParameter("invoiceID"))));

        invoice = getInvoiceFromStandardWR(wr, invoice);

        dao().invoiceDAO().update(invoice);

        System.out.println("update: " + invoice.getBillPeriod().getPeriodID());

        addAttributesToModel(model, invoice);
        return "redirect:invoices";
    }


    /*
        This method gets and sets the invoice attributes from a WebRequest.
        When creating and updating invoices, some of the same parameters need to be extracted from a WebRequest
        and this method reduces redundancy.
    */
    public Invoice getInvoiceFromStandardWR(WebRequest wr, Invoice invoice)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Period billPeriod;
        Period reservationPeriod;
        Motorhome motorhome;
        String customerID;

        //When updating, it is necessary to remember the period ids because they are fk's
        int billPeriodID = invoice.getBillPeriod().getPeriodID();
        int reservationPeriodID = invoice.getReservationPeriod().getPeriodID();

        customerID = wr.getParameter("customer");
        billPeriod = new Period(LocalDate.parse(wr.getParameter("billPeriodStartDate"), dtf), LocalDate.parse(wr.getParameter("billPeriodEndDate"), dtf));
        reservationPeriod = new Period(LocalDate.parse(wr.getParameter("reservationPeriodStartDate"), dtf), LocalDate.parse(wr.getParameter("reservationPeriodEndDate"), dtf));
        motorhome = dao().motorhomeDAO().read(Integer.parseInt(wr.getParameter("motorhome")));

        invoice.setCustomerID(customerID);
        invoice.setBillPeriod(billPeriod);
        invoice.setReservationPeriod(reservationPeriod);
        invoice.setMotorhome(motorhome);

        invoice.getBillPeriod().setPeriodID(billPeriodID);
        invoice.getReservationPeriod().setPeriodID(reservationPeriodID);
        return invoice;
    }

    /*
        The method responsible for deleting an invoice.
     */
    @GetMapping("invoices/delete")
    public String deleteService(@RequestParam int id)
    {
        dao().invoiceDAO().delete(id);
        return "redirect:invoices";
    }

    /*
        When the user adds a new service inside invoices/edit.html this method is called.
        It
     */
    @PostMapping("invoices/update/addservice")
    public String addServiceToInvoiceUpdate(@RequestParam int invoiceID,
                                            @RequestParam int serviceID,
                                            Model model, WebRequest wr, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoiceUpdate");

        Service service = dao().serviceDAO().read(serviceID);

        invoice.getServices().add(service);

        invoice = getInvoiceFromServiceWR(wr, invoice);

        addAttributesToModel(model, invoice);
        return "invoices/edit";
    }

    public Invoice getInvoiceFromServiceWR(WebRequest wr, Invoice invoice)
    {
        String customerID;
        LocalDate billStartDate;
        LocalDate billEndDate;
        LocalDate reservationStartDate;
        LocalDate reservationEndDate;
        Motorhome motorhome;
        Period billPeriod;
        Period reservationPeriod;
        int billPeriodID;
        int reservationPeriodID;

        billPeriodID = invoice.getBillPeriod().getPeriodID();
        reservationPeriodID = invoice.getReservationPeriod().getPeriodID();

        customerID = (wr.getParameter("customerID-service").equals("")) ? invoice.getCustomerID() : wr.getParameter("customerID-service");

        motorhome = (wr.getParameter("motorhomeID-service").equals("")) ? invoice.getMotorhome() :
                dao().motorhomeDAO().read(Integer.parseInt(wr.getParameter("motorhomeID-service")));

        billStartDate = (wr.getParameter("billPeriodStartDate-service").equals("")) ? invoice.getBillPeriod().getStart() : LocalDate.parse(wr.getParameter("billPeriodStartDate-service"));

        billEndDate = (wr.getParameter("billPeriodEndDate-service").equals("")) ? invoice.getBillPeriod().getEnd() : LocalDate.parse(wr.getParameter("billPeriodEndDate-service"));

        billPeriod = new Period(billStartDate, billEndDate);

        reservationStartDate = (wr.getParameter("reservationPeriodStartDate-service").equals("")) ? invoice.getReservationPeriod().getStart() : LocalDate.parse(wr.getParameter("reservationPeriodStartDate-service"));

        reservationEndDate = (wr.getParameter("reservationPeriodEndDate-service").equals("")) ? invoice.getReservationPeriod().getEnd() : LocalDate.parse(wr.getParameter("reservationPeriodEndDate-service"));

        reservationPeriod = new Period(reservationStartDate, reservationEndDate);

        invoice.setCustomerID(customerID);
        invoice.setBillPeriod(billPeriod);
        invoice.setReservationPeriod(reservationPeriod);
        invoice.setMotorhome(motorhome);

        invoice.getBillPeriod().setPeriodID(billPeriodID);
        invoice.getReservationPeriod().setPeriodID(reservationPeriodID);

        return invoice;
    }

    @PostMapping("invoices/update/removeservice")
    public String removeServiceFromInvoiceUpdate(@RequestParam int invoiceID,
                                                 @RequestParam int serviceID,
                                                 Model model, WebRequest wr, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoiceUpdate");

        for(int i = 0; i < invoice.getServices().size(); i++)
        {
            if(invoice.getServices().get(i).getServiceID() == serviceID)
            {
                invoice.getServices().remove(i);
                break;
            }
        }

        invoice = getInvoiceFromServiceWR(wr, invoice);

        addAttributesToModel(model, invoice);

        return "invoices/edit";
    }

    @GetMapping("invoices/create")
    public String showCreateForm(Model model, HttpSession session)
    {
        Invoice invoice = new Invoice();
        invoice.setServices(new ArrayList<Service>());
        invoice.setBillPeriod(new Period());
        invoice.setReservationPeriod(new Period());

        session.setAttribute("invoice", invoice);
        addAttributesToModel(model, invoice);

        return "invoices/new";
    }

    @PostMapping("invoices/create")
    public String createNewInvoice(WebRequest wr, Model model, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoice");
        invoice = getInvoiceFromStandardWR(wr, invoice);
        dao().invoiceDAO().create(invoice);
        addAttributesToModel(model, invoice);
        return "redirect:invoices";
    }

    @PostMapping("invoices/create/addservice")
    public String addServiceToNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoice");

        invoice.getServices().add(dao().serviceDAO().read(serviceID));

        invoice = getInvoiceFromServiceWR(wr, invoice);

        addAttributesToModel(model, invoice);
        return "invoices/new";
    }

    @PostMapping("invoices/create/removeservice")
    public String removeServiceFromNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr, HttpSession session)
    {
        Invoice invoice = (Invoice)session.getAttribute("invoice");

        invoice.getServices().remove(dao().serviceDAO().read(serviceID));

        invoice = getInvoiceFromServiceWR(wr, invoice);

        addAttributesToModel(model, invoice);

        return "invoices/new";
    }

    /**
     * Returns the invoice 'edit' view, displaying invoice for reservation.
     * Methods adds an invoice based on supplied reservation id to database, and returns
     * a view of this new invoice. If reservationID does not match any reservations in system
     * simply return a view with a new invoice for editing/disposal.
     */
    @PostMapping("invoices/fromreservation")
    public String createInvoiceFromReservation(@RequestParam int reservationID,
                                               Model model, HttpSession session)
    {
        Reservation r = dao().reservationDAO().read(reservationID);
        Invoice invoice;
        InvoiceDAO invoiceDAO = new InvoiceDAO();

        if(r == null) // if reservation id was bad, return ordinary empty create invoice view
        {
            return "redirect:invoices/create";
        }

        int tempID = dao().invoiceDAO().readall().size() + 1; // mechanism good for temp ids? with db?


        invoice = new Invoice(tempID,
                              r.getCustomer().getDriversLicence(),
                              new Period(LocalDate.now(), LocalDate.now().plusWeeks(2)),
                              r.getMotorhome(),
                              r.getServices(),
                              false,
                              r.getPeriod());

        //The invoice needs to match the invoice in the database that was just created.
        invoice = invoiceDAO.read(invoiceDAO.create_getID(invoice));

        // the view demands some lists, and we want no choices, only display of specific options.
        List<Motorhome> motorhomes = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Service> services = new ArrayList<>(dao().serviceDAO().readall());
        motorhomes.add(r.getMotorhome());
        customers.add(r.getCustomer());
        model.addAttribute("motorhomes", motorhomes);
        model.addAttribute("customers", customers);
        model.addAttribute("services", services);

        model.addAttribute("invoice", invoice);

        session.setAttribute("invoiceUpdate", invoice);

        return "invoices/edit";
    }

    @PostMapping("invoices/sendinvoice")
    public String sendInvoiceToCustomer(@RequestParam int invoiceID, Model model)
    {

        Invoice invoice = dao().invoiceDAO().read(invoiceID);

        if(invoice != null)
        {
            sendInvoiceEmailToCustomer(invoice);
            model.addAttribute("feedbackMessage",
                               "Invoice " + invoice.getInvoiceID() + " has been sent to " + invoice.getCustomerID());

        } else
        {
            model.addAttribute("feedbackMessage",
                               "Invoice " + invoice.getInvoiceID() + " COULD NOT BE sent to " + invoice.getCustomerID() +
                               " :: Reason: Invoice not found.");
        }

        return "feedback";
    }

    /* Method sends a confirmation email to the customer. */
    private void sendInvoiceEmailToCustomer(Invoice invoice)
    {
        PreparedOutGoingMessage prepared = new PreparedOutGoingMessage();
        String message = prepared.getInvoiceEmailText(invoice); // message returned is not complete

        SimpleMessageSender sender = SimpleMessageSender.motorhomeStandardConnection();

        Customer customer = dao().customerDAO().read(invoice.getCustomerID());

        sender.sendEmail(customer.getPerson().getEmail(),
                         "Invoice for " + customer.getPerson().getFullName()
                         + " from Nordic Motorhome Rental", message);
    }


}
