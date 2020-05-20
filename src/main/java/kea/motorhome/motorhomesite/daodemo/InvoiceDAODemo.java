package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAODemo implements IDAO<Invoice, Integer> {

    List<Invoice> invoices;

    public InvoiceDAODemo() {
        invoices = new ArrayList<>();
        // new Period
        Period period1 = new Period(LocalDate.now().minusDays(10), LocalDate.now().minusYears(5));
        // new Service list
        List<Service> services1 = new ArrayList<Service>();
        services1.add(new Service(1, "No, you're great!", 32, "Great Service"));
        // add Invoice
        invoices.add(new Invoice(1, "1234-1234-1234-1234", period1, services1));

        invoices = new ArrayList<>();
        // new Period
        Period period2 = new Period(LocalDate.now().minusDays(50), LocalDate.now().minusYears(1));
        // new Service list
        List<Service> services2 = new ArrayList<Service>();
        services1.add(new Service(2, "You've been bad :P", 430, "Bad Service"));
        // add Invoice
        invoices.add(new Invoice(2, "1234-1234-1234-1234", period2, services2));

    }

    @Override
    public boolean create(Invoice thing) {
        return invoices.add(thing);
    }

    @Override
    public Invoice read(Integer id)
    {
        for (Invoice invoice : invoices)
            if (invoice.getInvoiceID() == id)
                return invoice;
        return null;
    }

    @Override
    public List<Invoice> readall() { return invoices; }

    @Override
    public boolean update(Invoice invoice) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getInvoiceID() == invoice.getInvoiceID()) {
                invoices.set(i, invoice);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (Invoice invoice : invoices) {
            if (id == invoice.getInvoiceID()) return invoices.remove(invoice);
        }
        return false;
    }
}
