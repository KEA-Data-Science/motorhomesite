package kea.motorhome.motorhomesite.dao;
// by LNS

import kea.motorhome.motorhomesite.models.*;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements IDAO<Customer, String>
{
    private Connection connection;

    public CustomerDAO(){ connection = DBConnectionManager.getConnection(); }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @Override
    public boolean create(Customer thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.customer (driversLicense, approved, Person_idPerson, Paycard_idPaycard) " +
                    "VALUES (?,?,?,?)");

            preparedStatement.setString(1, thing.getDriversLicence());
            byte approvedAsByte = (byte)(thing.isApproved() ? 1 : 0); // Byte to represent boolean value in database
            preparedStatement.setByte(2, approvedAsByte);
            preparedStatement.setInt(3, thing.getPerson().getPersonID()); // Sets id of Person
            preparedStatement.setInt(4, thing.getPayCard().getCardID()); // Sets id of PayCard

            return preparedStatement.executeUpdate() > 0;
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public Customer read(String driversLicense)
    {
        Customer customer = new Customer();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.customer WHERE driversLicense = ?");
            preparedStatement.setString(1, driversLicense);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) { fillCustomerValuesFromResultSet(customer, resultSet); }

            return customer;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    private void fillCustomerValuesFromResultSet(Customer customer, ResultSet resultSet) throws SQLException
    {
        customer.setCustomerID(resultSet.getInt(1));
        customer.setDriversLicence(resultSet.getString(2));
        customer.setApproved(resultSet.getBoolean(3));
        // Sets appropriate Person object through its id
        customer.setPerson(dao().personDAO().read(resultSet.getInt(4)));
        // Sets appropriate PayCard object through its id
        customer.setPayCard(dao().paycardDAO().read(resultSet.getInt(5)));
    }


    @Override
    public List<Customer> readall()
    {
        List<Customer> customers = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.customer");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Customer customer = new Customer();
                fillCustomerValuesFromResultSet(customer, resultSet);
                customers.add(customer);
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return customers;
    }

    /**
     * Update only able to change/update approved-status; all other fields are foreign keys
     * (not idCustomer, but you don't want to change that)
     */
    @Override
    public boolean update(Customer thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE `motorhome`.`customer` SET `approved` = ? WHERE (`driversLicense` = ?)");

            byte approvedAsByte = (byte)(thing.isApproved() ? 1 : 0); // Byte to represent boolean value in database
            preparedStatement.setByte(1, approvedAsByte);
            preparedStatement.setString(2, thing.getDriversLicence());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public boolean delete(String driversLicense)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.customer WHERE driversLicense = ?");
            preparedStatement.setString(1, driversLicense);

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}