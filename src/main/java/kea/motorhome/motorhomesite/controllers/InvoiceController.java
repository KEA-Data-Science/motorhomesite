package kea.motorhome.motorhomesite.controllers;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InvoiceController
{

    SiteDAOCollection dao;

    public InvoiceController()
    {
        dao = SiteDAOCollection.getInstance();
    }

    @GetMapping("/invoices")
    public String showInvoices(Model model)
    {
        List<Invoice> invoices = new ArrayList<>();
        SiteDAOCollection dao = SiteDAOCollection.getInstance();
        PriceCalculator priceCalculator = new PriceCalculator();

        for(Invoice invoice : dao.invoiceDAO().readall())
        {
            if(invoice.isCompleted())
                invoices.add(invoice);
        }

        model.addAttribute("invoices", invoices);
        model.addAttribute("calculator", priceCalculator);

        return "/invoices/invoices";
    }

    @GetMapping("/invoices/update")
    public String updateinvoice(@RequestParam int id, Model model)
    {
        addAttributesToModel(model, id, null);
        return "/invoices/edit";
    }

    public void addAttributesToModel(Model model, int id, Invoice invoice)
    {
        if(invoice == null)
            model.addAttribute("invoice", dao.invoiceDAO().read(id));
        else
            model.addAttribute("invoice", invoice);

        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());
    }

    @PostMapping("/invoices/perfomupdate")
    public String performUpdate(WebRequest wr, Model model)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Invoice invoice = getInvoiceFromWR(wr, null);

        dao.invoiceDAO().update(invoice);

        return "redirect:/invoices";
    }

    public Invoice getInvoiceFromWR(WebRequest wr, Invoice invoice)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int invoiceID = -200;
        Period billPeriod;
        Period reservationPeriod;
        Motorhome motorhome;
        String customerID;

        if(invoice == null)
            invoiceID = Integer.parseInt((wr.getParameter("invoiceID")));

        customerID = wr.getParameter("customer");
        billPeriod = new Period(LocalDate.parse(wr.getParameter("billPeriodStartDate"), dtf), LocalDate.parse(wr.getParameter("billPeriodEndDate"), dtf));
        reservationPeriod = new Period(LocalDate.parse(wr.getParameter("reservationPeriodStartDate"), dtf), LocalDate.parse(wr.getParameter("reservationPeriodEndDate"), dtf));
        motorhome = dao.motorhomeDAO().read(Integer.parseInt(wr.getParameter("motorhome")));

        if(invoice == null)
            return new Invoice(invoiceID, customerID, billPeriod, motorhome, dao.invoiceDAO().read(invoiceID).getServices(), true, reservationPeriod);

        invoice.setCustomerID(customerID);
        invoice.setBillPeriod(billPeriod);
        invoice.setReservationPeriod(reservationPeriod);
        invoice.setMotorhome(motorhome);
        return invoice;
    }

    @GetMapping("/invoices/delete")
    public String deleteService(@RequestParam int id)
    {
        dao.invoiceDAO().delete(id);
        return "redirect:/invoices";
    }

    @PostMapping("/invoices/update/addservice")
    public String addServiceToInvoiceUpdate(@RequestParam int invoiceID,
                                            @RequestParam int serviceID,
                                            Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        Service service = dao.serviceDAO().read(serviceID);

        invoice.getServices().add(service); // multiple copies of the same service is possible on 1 res.

        invoice = getInvoiceFromWRService(wr, invoice);

        addAttributesToModel(model, invoiceID, invoice);

        return "invoices/edit";
    }

    @PostMapping("/invoices/update/removeservice")
    public String removeServiceFromInvoiceUpdate(@RequestParam int invoiceID,
                                                 @RequestParam int serviceID,
                                                 Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        for(int i = 0; i < invoice.getServices().size(); i++)
        {
            if(invoice.getServices().get(i).getServiceID() == serviceID)
            {
                invoice.getServices().remove(i);
                break;
            }
        }

        invoice = getInvoiceFromWRService(wr, invoice);

        addAttributesToModel(model, invoiceID, invoice);

        return "invoices/edit";
    }

    @GetMapping("/invoices/create")
    public String showCreateForm(Model model)
    {
        Invoice invoice;

        if((invoice = dao.invoiceDAO().read(-200)) == null)
        {
            System.out.println("create");
            invoice = new Invoice();
            invoice.setServices(new ArrayList<Service>());
            invoice.setCompleted(false);
            invoice.setInvoiceID(-200);
            invoice.setBillPeriod(new Period());
            invoice.setReservationPeriod(new Period());
        }
        dao.invoiceDAO().readall().add(invoice);
        addAttributesToModel(model, -200, invoice);

        return "/invoices/new";
    }

    @PostMapping("/invoices/create")
    public String createNewInvoice(WebRequest wr, Model model)
    {
        Invoice invoice = dao.invoiceDAO().read(-200); //
        invoice.setCompleted(true); // fra KCN; er regningen betalt?
        System.out.println(invoice);
        invoice = getInvoiceFromWR(wr, invoice);
        System.out.println(invoice);
        dao.invoiceDAO().delete(-200);
        invoice.setInvoiceID(dao.invoiceDAO().readall().size() + 1);
        dao.invoiceDAO().create(invoice);
        addAttributesToModel(model, invoice.getInvoiceID(), invoice);
        return "redirect:/invoices";
    }

    @PostMapping("/invoices/create/addservice")
    public String addServiceToNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(-200);
        System.out.println("Before wr: "+ invoice);

        invoice.getServices().add(dao.serviceDAO().read(serviceID));

        invoice = getInvoiceFromWRService(wr, invoice);
        System.out.println("After wr: "+ invoice);

        addAttributesToModel(model, -200, invoice);
        return "/invoices/new";
    }

    public Invoice getInvoiceFromWRService(WebRequest wr, Invoice invoice)
    {
        String customerID;
        LocalDate billStartDate;
        LocalDate billEndDate;
        LocalDate reservationStartDate;
        LocalDate reservationEndDate;
        Motorhome motorhome;
        Period billPeriod;
        Period reservationPeriod;

        customerID =  (wr.getParameter("customerID-service").equals("")) ? invoice.getCustomerID() :  wr.getParameter("customerID-service");

        motorhome = (wr.getParameter("motorhomeID-service").equals("")) ? invoice.getMotorhome() : dao.motorhomeDAO().read(Integer.parseInt(wr.getParameter("motorhomeID-service")));

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

        return invoice;
    }

    @PostMapping("/invoices/create/removeservice")
    public String removeServiceFromNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(-200);

        invoice.getServices().remove(dao.serviceDAO().read(serviceID));

        invoice = getInvoiceFromWRService(wr, invoice);

        addAttributesToModel(model, -200, invoice);

        return "/invoices/new";
    }

    /**
     * Returns the invoice 'edit' view, displaying invoice for reservation.
     * Methods adds an invoice based on supplied reservation id to database, and returns
     * a view of this new invoice. If reservationID does not match any reservations in system
     * simply return a view with a new invoice for editing/disposal.
     */
    @PostMapping("/invoices/fromreservation")
    public String createInvoiceFromReservation(@RequestParam int reservationID,
                                               Model model)
    {
        Reservation r = dao.reservationDAO().read(reservationID);
        Invoice invoice;

        if(r == null) // if reservation id was bad, return ordinary empty create invoice view
        {
            invoice = dao.invoiceDAO().read(-200);
            addAttributesToModel(model, -200, invoice);
            return "redirect:/invoices/create";
        }

        int tempID = dao.invoiceDAO().readall().size() + 1; // mechanism good for temp ids? with db?

        invoice = new Invoice(tempID,
                              r.getCustomer().getDriversLicence(),
                              new Period(LocalDate.now(), LocalDate.now().plusWeeks(2)),
                              r.getMotorhome(),
                              r.getServices(),
                              false,
                              r.getPeriod());

        dao.invoiceDAO().create(invoice); /* entering invoice from reservation into database */

        // the view demands some lists, and we want no choices, only display of specific options.
        List<Motorhome> motorhomes = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Service> services = new ArrayList<>(dao.serviceDAO().readall());
        motorhomes.add(r.getMotorhome());
        customers.add(r.getCustomer());
        model.addAttribute("motorhomes", motorhomes);
        model.addAttribute("customers", customers);
        model.addAttribute("services", services);

        model.addAttribute("invoice", invoice);

        return "/invoices/edit";
    }

    @PostMapping("/invoices/sendinvoice")
    public String sendInvoiceToCustomer(@RequestParam int invoiceID, Model model)
    {

        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        if(invoice != null)
        {
            sendInvoiceEmailToCustomer(invoice);
            model.addAttribute("feedbackMessage",
                               "Invoice " + invoice.getInvoiceID() + " has been sent to " + invoice.getCustomerID());

        } else{
            model.addAttribute("feedbackMessage",
                               "Invoice " + invoice.getInvoiceID() + " COULD NOT BE sent to " + invoice.getCustomerID() +
                               " :: Reason: Invoice not found.");
        }

        return "/feedback";
    }

    /* Method sends a confirmation email to the customer. */
    private void sendInvoiceEmailToCustomer(Invoice invoice)
    {
        PreparedOutGoingMessage prepared = new PreparedOutGoingMessage();
        String message = prepared.getInvoiceEmailText(invoice); // message returned is not complete

        SimpleMessageSender sender = SimpleMessageSender.motorhomeStandardConnection();

        Customer customer = dao.customerDAO().read(invoice.getCustomerID());

        sender.sendEmail(customer.getPerson().getEmail(),
                         "Invoice for " + customer.getPerson().getFullName()
                         + " from Nordic Motorhome Rental", message);
    }

}
