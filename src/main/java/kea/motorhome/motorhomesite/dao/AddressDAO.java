package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.PayCard;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class instance is a data access object that facilitates contact between the
 * model layer and the data layer and business logic layer.
 * Construction comments in PersonDAO-class exaclty parallel, not reproduced.
 */
public class AddressDAO implements IDAO<Address, Integer>
{
    private Connection connection;

    public AddressDAO()
    {
        connection = DBConnectionManager.getConnection();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(Address thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.address (idAddress, country, roadName, houseNumber, postCode) " +
                    "VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, thing.getAddressID());
            preparedStatement.setString(2, thing.getCountry());
            preparedStatement.setString(3, thing.getRoadName());
            preparedStatement.setString(4, thing.getHouseNumber());
            preparedStatement.setString(5, thing.getPostCode());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public Address read(Integer id)
    {
        Address address = new Address();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.address WHERE idAddress = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) { fillAddressValuesFromResultSet(address, resultSet); }

            return address;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    private void fillAddressValuesFromResultSet(Address address, ResultSet resultSet) throws SQLException
    {
        address.setAddressID(resultSet.getInt(1));
        address.setCountry(resultSet.getString(2));
        address.setRoadName(resultSet.getString(3));
        address.setHouseNumber(resultSet.getString(4));
        address.setPostCode(resultSet.getString(5));
    }

    @Override
    public List<Address> readall()
    {
        List<Address> addresses = new ArrayList<>();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.address");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Address address = new Address();
                fillAddressValuesFromResultSet(address, resultSet);
                addresses.add(address);
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return addresses;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Address thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.address SET " +
                    "country = ?," +
                    "roadName = ?," +
                    "houseNumber = ?," +
                    "postCode = ?" +
                    "WHERE idAddress = ?");

            preparedStatement.setString(1, thing.getCountry());
            preparedStatement.setString(2, thing.getRoadName());
            preparedStatement.setString(3, thing.getHouseNumber());
            preparedStatement.setString(4, thing.getPostCode());
            preparedStatement.setInt(5, thing.getAddressID());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.address WHERE idAddress = ?");
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}