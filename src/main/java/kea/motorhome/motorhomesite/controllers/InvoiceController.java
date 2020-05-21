package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.InvoiceDAODemo;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.format.annotation.DateTimeFormat;
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
public class InvoiceController {

    SiteDAOCollection dao;

    public InvoiceController(){dao = SiteDAOCollection.getInstance();}
    @GetMapping("/invoices")
    public String showInvoices(Model model)
    {
        SiteDAOCollection dao = SiteDAOCollection.getInstance();
        PriceCalculator priceCalculator = new PriceCalculator();
        model.addAttribute("invoices", dao.invoiceDAO().readall());
        model.addAttribute("calculator", priceCalculator);
        return "/invoices/invoices";
    }

    @GetMapping("/updateinvoice")
    public String updateinvoice(@RequestParam int id, Model model)
    {
        model.addAttribute("invoice", dao.invoiceDAO().read(id));
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());
        System.out.println(dao.invoiceDAO().read(id).getServices().toString());
        return "/invoices/edit";
    }

    @PostMapping("/invoices/updateinvoice")
    public String performUpdate(WebRequest wr, Model model)
    {
        PriceCalculator priceCalculator = new PriceCalculator();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int invoiceID = Integer.parseInt(wr.getParameter("invoiceID"));
        String customerID = wr.getParameter("customer");
        LocalDate startDate = LocalDate.parse(wr.getParameter("startDate"), dtf);
        LocalDate endDate = LocalDate.parse(wr.getParameter("endDate"), dtf);
        Period period = new Period(startDate, endDate);
        Motorhome motorhome = dao.motorhomeDAO().read(Integer.parseInt(wr.getParameter("motorhome")));
        Invoice invoice = new Invoice(invoiceID, customerID, period, motorhome, new ArrayList<Service>());

        dao.invoiceDAO().update(invoice);

        System.out.println(wr.getParameter("startDate"));
        //Invoice invoice = new Invoice(Integer.parseInt(wr.getParameter("invoiceID"))
        model.addAttribute("invoice", dao.invoiceDAO().read(invoiceID));

        model.addAttribute("invoices", dao.invoiceDAO().readall());
        model.addAttribute("invoice", dao.invoiceDAO().read(invoiceID));
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());
        model.addAttribute("calculator", priceCalculator);

        return "invoices/invoices";
    }

    @GetMapping("/deleteinvoice")
    public String deleteService(@RequestParam int id)
    {
        dao.invoiceDAO().delete(id);
        return "redirect:/invoices";
    }

    @PostMapping("invoice/addservice")
    public String addServiceToReservation(@RequestParam int invoiceID,
                                          @RequestParam int serviceID,
                                          Model model)
    {
        Invoice invoice = dao.invoiceDAO().read(invoiceID);

        Service service = dao.serviceDAO().read(serviceID);

        invoice.getServices().add(service); // multiple copies of the same service is possible on 1 res.

        model.addAttribute("invoice", dao.invoiceDAO().read(invoiceID));
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());

        System.out.println("Called");

        return "invoices/edit";
    }

    @PostMapping("invoice/removeservice")
    public String removeServicefromInvoice(@RequestParam int invoiceID,
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

        List<Invoice> invoices = dao.invoiceDAO().readall();

        model.addAttribute("invoice", dao.invoiceDAO().read(invoiceID));
        model.addAttribute("customers", dao.customerDAO().readall());
        model.addAttribute("motorhomes", dao.motorhomeDAO().readall());
        model.addAttribute("services", dao.serviceDAO().readall());

        return "invoices/edit";
    }
}
