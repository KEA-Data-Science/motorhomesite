package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.PriceCalculator;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.InvoiceDAODemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvoiceController {

    @GetMapping("/invoices")
    public String showInvoices(Model model)
    {
        SiteDAOCollection dao = SiteDAOCollection.getInstance();
        PriceCalculator priceCalculator = new PriceCalculator();
        model.addAttribute("invoices", dao.invoiceDAO().readall());
        model.addAttribute("calculator", priceCalculator);
        return "/invoices/invoices";
    }
}
