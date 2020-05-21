package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.InvoiceDAODemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showUpdateService(@RequestParam int id, Model model)
    {
        model.addAttribute("invoice", dao.invoiceDAO().read(id));
        return "/invoices/updateinvoice";
    }
}
