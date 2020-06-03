package kea.motorhome.motorhomesite.dao;
// by LNS
import kea.motorhome.motorhomesite.models.Invoice;
import kea.motorhome.motorhomesite.models.Service;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements IDAO<Invoice,Integer> {

    private Connection connection;

    public InvoiceDAO()
    {
        connection = DBConnectionManager.getConnection();
    }

    @Override
    public boolean create(Invoice thing) {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.invoice (isCompleted, Customer_driversLicense, billPeriod, Motorhome_IdMotorhome, reservationPeriod) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setBoolean(1, thing.isCompleted());
            preparedStatement.setString(2, thing.getCustomerID());
            preparedStatement.setInt(3, new PeriodDAO().create_getID(thing.getBillPeriod()));
            preparedStatement.setInt(4, thing.getMotorhome().getMotorhomeID());
            preparedStatement.setInt(5, new PeriodDAO().create_getID(thing.getReservationPeriod()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
            {
                thing.setInvoiceID(resultSet.getInt(1));
            }
            return (addServices(thing));


        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    public int create_getID(Invoice thing) {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.invoice (isCompleted, Customer_driversLicense, billPeriod, Motorhome_IdMotorhome, reservationPeriod) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setBoolean(1, thing.isCompleted());
            preparedStatement.setString(2, thing.getCustomerID());
            preparedStatement.setInt(3, new PeriodDAO().create_getID(thing.getBillPeriod()));
            preparedStatement.setInt(4, thing.getMotorhome().getMotorhomeID());
            preparedStatement.setInt(5, new PeriodDAO().create_getID(thing.getReservationPeriod()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
            {
                thing.setInvoiceID(resultSet.getInt(1));
            }

            return thing.getInvoiceID();


        } catch(SQLException e) { e.printStackTrace(); }

        return -1;

    }



    /*
        This method adds all the services in the supplied Invoice object to the junction table Invoice_has_Service and gives the rows
        an invoiceID matching the Invoice parameter.
     */
    public boolean addServices(Invoice thing)
    {
        //If there are no services in the invoice, do nothing.
        if (thing.getServices().size() == 0) { return true; }

        try
        {
            //Add all of the services in the invoice to the junction table
            for (Service service : thing.getServices())
            {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO motorhome.Invoice_has_Service (Invoice_idInvoice, Service_idService) "+
                                "VALUES (?,?)");
                preparedStatement.setInt(1, thing.getInvoiceID());
                preparedStatement.setInt(2, service.getServiceID());
                preparedStatement.executeUpdate();
            }

            return true;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }


    public boolean deleteServices(Invoice thing)
    {
        try {
            //Delete all services related to the supplied invoice
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.Invoice_has_Service " +
                            "WHERE Invoice_idInvoice = ?");

            preparedStatement.setInt(1, thing.getInvoiceID());

            preparedStatement.executeUpdate();

            return true;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    /*
        Get a list of all services in the junction table Invoice_has_Service with an invoice id matching the parameter
     */
    public List<Service> readAllServices(Invoice thing)
    {
        List<Service> services = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Invoice_has_Service " +
                    "WHERE Invoice_idInvoice = ?");

            preparedStatement.setInt(1, thing.getInvoiceID());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Service service = (new ServiceDAO().read(resultSet.getInt("Service_idService")));

                services.add(service);
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return services;
    }

    private void fillInInvoiceValues(Invoice invoice, ResultSet resultSet)
    {
        try
        {
            invoice.setInvoiceID(resultSet.getInt(1));
            invoice.setCompleted(resultSet.getBoolean(2));
            invoice.setBillPeriod(new PeriodDAO().read(resultSet.getInt(3)));
            invoice.setMotorhome(new MotorhomeDAO().read(resultSet.getInt(4)));
            invoice.setReservationPeriod(new PeriodDAO().read(resultSet.getInt(5)));
            invoice.setCustomerID(resultSet.getString(6));
            invoice.setServices(readAllServices(invoice));

        } catch(SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Invoice read(Integer id) {
        Invoice invoice = new Invoice();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.invoice WHERE idInvoice = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) { fillInInvoiceValues(invoice, resultSet); }

            return invoice;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    @Override
    public List<Invoice> readall() {
        List<Invoice> invoices = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM motorhome.invoice");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Invoice invoice = new Invoice();
                fillInInvoiceValues(invoice, resultSet);
                invoices.add(invoice);
            }

        } catch(SQLException e) { e.printStackTrace(); }
        return invoices;
    }

    @Override
    public boolean update(Invoice thing) {
        try
        {
            PeriodDAO periodDAO = new PeriodDAO();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.invoice SET " +
                            "isCompleted = ?," +
                            "Motorhome_IdMotorhome = ?, " +
                            "Customer_driversLicense = ? " +
                            "WHERE idInvoice = ?");
            preparedStatement.setBoolean(1, thing.isCompleted());
            preparedStatement.setInt(2, thing.getMotorhome().getMotorhomeID());
            preparedStatement.setString(3, thing.getCustomerID());
            preparedStatement.setInt(4, thing.getInvoiceID());

            //Instead of updating all rows in the junction table related to this invoice,
            //they first get deleted and then the current list of services gets written to the table.
            deleteServices(thing);
            addServices(thing);

            periodDAO.update(thing.getBillPeriod());
            periodDAO.update(thing.getReservationPeriod());

            int numChanges = preparedStatement.executeUpdate();
            return numChanges > 0;
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        try
        {
            if (deleteServices(read(id)) == false) return false;

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.invoice WHERE idInvoice = ?");

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

}