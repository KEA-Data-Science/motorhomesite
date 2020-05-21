package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Invoice;
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.models.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAODemo implements IDAO<Invoice, Integer> {
    List<Invoice> invoices;

    public InvoiceDAODemo()
    {
        invoices = new ArrayList<>();

    }
    @Override
    public boolean create(Invoice thing) {
        return invoices.add(thing);
    }

    @Override
    public List<Invoice> readall()
    {
        return invoices;
    }

    public Invoice read(Integer id)
    {
        for (Invoice invoice : invoices)
            if (invoice.getInvoiceID() == id)
                return invoice;
        return null;
    }

    @Override
    public boolean update(Invoice invoice)
    {
        for (int i = 0; i < invoices.size(); i++)
        {
            if (invoices.get(i).getInvoiceID() == invoice.getInvoiceID()) {
                invoices.set(i, invoice);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (Invoice invoice : invoices)
        {
            if (id == invoice.getInvoiceID())
                return invoices.remove(invoice);
        }
        return false;
    }
}
