package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Invoice;

import java.sql.Connection;
import java.util.List;

public class InvoiceDAO implements IDAO<Invoice,Integer> {

    private Connection connection;

    @Override
    public boolean create(Invoice thing) {
        return false;
    }

    @Override
    public Invoice read(Integer id) {
        return null;
    }

    @Override
    public List<Invoice> readall() {
        return null;
    }

    @Override
    public boolean update(Invoice thing) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
