package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.PayCard;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

/**
 * Class instance is a data access object that facilitates contact between the
 * model layer and the data layer and business logic layer.
 * Construction comments in PersonDAO-class exaclty parallel, not reproduced.
 */
public class PayCardDAO implements IDAO<PayCard, Integer>
{
    /* sql connection to db motorhome */
    private Connection connection;


    public PayCardDAO()
    {
        connection = DBConnectionManager.getConnection(); // singlton instance
    }

    /**
     * Method writes an entity to motorhome.paycard table based on supplied PayCard-thing.
     * Returns the number of rows affected/written, zero not good.
     *
     * @param thing
     */
    @Override
    public boolean create(PayCard thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.paycard (cardType, cardNumber, expirationDate, securityDigits) " +
                    "VALUES (?,?,?,?)");

            preparedStatement.setString(1, thing.getCardType());
            preparedStatement.setString(2, thing.getCardNumber());
            preparedStatement.setDate(3, Date.valueOf(thing.getExpirationDate()));
            preparedStatement.setInt(4, thing.getSecurityDigits());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public PayCard read(Integer id)
    {
        PayCard payCard = new PayCard();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.paycard WHERE idPaycard = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) { fillPayCardValuesFromResultSet(payCard, resultSet); }

            return payCard;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    private void fillPayCardValuesFromResultSet(PayCard payCard, ResultSet resultSet) throws SQLException
    {
        payCard.setCardID(resultSet.getInt(1));
        payCard.setCardType(resultSet.getString(2));
        payCard.setCardNumber(resultSet.getString(3));
        payCard.setExpirationDate(resultSet.getDate(4).toLocalDate());
        payCard.setSecurityDigits(resultSet.getInt(5));
    }

    @Override
    public List<PayCard> readall()
    {
        List<PayCard> payCards = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.paycard");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                PayCard card = new PayCard();
                fillPayCardValuesFromResultSet(card, resultSet);
                payCards.add(card);
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return payCards;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(PayCard thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.paycard SET " +
                    "cardType = ?," +
                    "cardNumber = ?," +
                    "expirationDate = ?," +
                    "securityDigits = ?" +
                    "WHERE idPaycard = ?");

            preparedStatement.setString(1, thing.getCardType());
            preparedStatement.setString(2, thing.getCardNumber());
            preparedStatement.setDate(3, Date.valueOf(thing.getExpirationDate()));
            preparedStatement.setInt(4, thing.getSecurityDigits());
            preparedStatement.setInt(5, thing.getCardID());

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
                    "DELETE FROM motorhome.paycard WHERE idPaycard = ?");
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}