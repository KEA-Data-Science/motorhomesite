package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.util.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.InvoiceDAODemo;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Controller
public class InvoiceController {

    SiteDAOCollection dao;

    public InvoiceController(){
        dao = SiteDAOCollection.getInstance();
    }

    @GetMapping("/invoices")
    public String showInvoices(Model model)
    {
        List<Invoice> invoices = new ArrayList<>();
        SiteDAOCollection dao = SiteDAOCollection.getInstance();
        PriceCalculator priceCalculator = new PriceCalculator();

        for (Invoice invoice : dao.invoiceDAO().readall()) {
            if (invoice.isCompleted())
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

    @PostMapping("/invoices/perfomupdate")
    public String performUpdate(WebRequest wr, Model model)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Invoice invoice = getInvoiceFromWR(wr, null, "startDate", "endDate",
                "motorhome", "customer");

        dao.invoiceDAO().update(invoice);

        return "redirect:/invoices";
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
                                          Model model)
    {
        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        Service service = dao.serviceDAO().read(serviceID);

        invoice.getServices().add(service); // multiple copies of the same service is possible on 1 res.

        addAttributesToModel(model, invoiceID, null);

        return "invoices/edit";
    }

    @PostMapping("/invoices/update/removeservice")
    public String removeServiceFromInvoiceUpdate(@RequestParam int invoiceID,
                                               @RequestParam int serviceID,
                                               Model model)
    {
        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        for(int i=0; i< invoice.getServices().size();i++)
        {
            if(invoice.getServices().get(i).getServiceID() == serviceID)
            {
                invoice.getServices().remove(i);
                break;
            }
        }

        addAttributesToModel(model, invoiceID, null);

        return "invoices/edit";
    }

    @GetMapping("/invoices/create")
    public String showCreateForm(Model model)
    {
        Invoice invoice;

        if ((invoice = dao.invoiceDAO().read(-200)) == null) {
            System.out.println("create");
            invoice = new Invoice();
            invoice.setServices(new ArrayList<Service>());
            invoice.setCompleted(false);
            invoice.setInvoiceID(-200);
        }
        dao.invoiceDAO().readall().add(invoice);
        addAttributesToModel(model, -200, invoice);

        return "/invoices/new";
    }

    @PostMapping("/invoices/create")
    public String createNewInvoice(WebRequest wr, Model model)
    {
        Invoice invoice = dao.invoiceDAO().read(-200);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PriceCalculator priceCalculator = new PriceCalculator();
        invoice.setCompleted(true);
        invoice = getInvoiceFromWR(wr, dao.invoiceDAO().read(-200), "startDate", "endDate", "motorhome", "customer" );
        dao.invoiceDAO().delete(-200);
        invoice.setInvoiceID(dao.invoiceDAO().readall().size()+1);
        dao.invoiceDAO().create(invoice);
        return "redirect:/invoices";
    }

    @PostMapping("/invoices/create/addservice")
    public String addServiceToNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(-200);

        invoice.getServices().add(dao.serviceDAO().read(serviceID));

        invoice = getInvoiceFromWRService(wr, invoice, "hidden-startdate-addservice","hidden-enddate-addservice",
                "hidden-motorhomeid-addservice", "hidden-customerid-addservice" );

        addAttributesToModel(model, -200, invoice);
        return "/invoices/new";
    }

    @PostMapping("/invoices/create/removeservice")
    public String removeServiceFromNewInvoice(@RequestParam int serviceID, Model model, WebRequest wr)
    {
        Invoice invoice = dao.invoiceDAO().read(-200);
        invoice.getServices().remove(dao.serviceDAO().read(serviceID));
        invoice = getInvoiceFromWRService(wr, invoice, "hidden-startdate-removeservice","hidden-enddate-removeservice",
                "hidden-motorhomeid-removeservice", "hidden-customerid-removeservice" );
        addAttributesToModel(model, 0, invoice);

        return "/invoices/new";
    }

    public Invoice getInvoiceFromWR(WebRequest wr, Invoice invoice, String startDateName, String endDateName, String motorhomeName, String customerName)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int invoiceID = -200;
        Period period;
        Motorhome motorhome;
        String customerID;

        if (invoice == null)
            invoiceID = Integer.parseInt((wr.getParameter("invoiceID")));

        customerID = wr.getParameter(customerName);
        period = new Period(LocalDate.parse(wr.getParameter(startDateName), dtf), LocalDate.parse(wr.getParameter(endDateName), dtf));
        motorhome = dao.motorhomeDAO().read(Integer.parseInt(wr.getParameter(motorhomeName)));

        if (invoice == null)
            return new Invoice(invoiceID, customerID, period, motorhome, dao.invoiceDAO().read(invoiceID).getServices(), true);

        invoice.setCustomerID(customerID);
        invoice.setBillPeriod(period);
        invoice.setMotorhome(motorhome);
        return invoice;
    }

    public Invoice getInvoiceFromWRService(WebRequest wr, Invoice invoice, String startDateName, String endDateName, String motorhomeName, String customerName)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String customerID = "";
        int motorhomeID = 0;
        LocalDate startDate;
        LocalDate endDate;
        Motorhome motorhome = null;
        Period period = null;


        if (wr.getParameter(customerName).equals(""))
            customerID = invoice.getCustomerID();
        else
            customerID = wr.getParameter(customerName);

        if (wr.getParameter(startDateName).equals("") && wr.getParameter(endDateName).equals("")) {
            period = invoice.getBillPeriod();
        }
        else {
        startDate = LocalDate.parse(wr.getParameter(startDateName));
        endDate = LocalDate.parse(wr.getParameter(endDateName));
        period = new Period(startDate, endDate);
        }

        if (wr.getParameter(motorhomeName).equals("")) {
            motorhome = invoice.getMotorhome();
        }
        else {
            motorhomeID = Integer.parseInt(wr.getParameter(motorhomeName));
            motorhome = dao.motorhomeDAO().read(motorhomeID);
        }

        invoice.setCustomerID(customerID);
        invoice.setBillPeriod(period);
        invoice.setMotorhome(motorhome);
        return invoice;
    }

    public void addAttributesToModel(Model model, int id, Invoice invoice)
    {
        if (invoice == null)
            model.addAttribute("invoice", dao.invoiceDAO().read(id));
        else
            model.addAttribute("invoice", invoice);

        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());
    }

}
