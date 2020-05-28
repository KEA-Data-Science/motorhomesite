package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.*;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements IDAO<Customer, String>
{
	private Connection connection;
	private SiteDAOCollection dao;

	public CustomerDAO()
	{
		connection = DBConnectionManager.getConnection();
		dao = SiteDAOCollection.getInstance();
	}

	@Override
	public boolean create(Customer thing) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO motorhome.customer (driversLicense, approved, Person_idPerson, Paycard_idPaycard) " +
							"VALUES (?,?,?,?)");

			preparedStatement.setString(1, thing.getDriversLicence());
			preparedStatement.setBoolean(2, thing.isApproved()); // TODO: Virker dette? Værdien skal gemmes som TINYINT(1) i databasen
			preparedStatement.setInt(3, thing.getPerson().getPersonID());
			preparedStatement.setInt(3, thing.getPayCard().getCardID());

			return preparedStatement.executeUpdate() > 0;
		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}

	private void fillCustomerValuesFromResultSet(Customer customer, ResultSet resultSet) throws SQLException {
		// TODO: I tabellen har customer også en idCustomer. Bør den også være med her?
		customer.setDriversLicence(resultSet.getString(2));
		customer.setApproved(resultSet.getBoolean(3));
		// Sets appropriate Person object through its id
		customer.setPerson(dao.personDAO().read(resultSet.getInt(3)));
		// Sets appropriate PayCard object through its id
		customer.setPayCard(dao.paycardDAO().read(resultSet.getInt(4)));
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

	@Override
	public boolean update(Customer thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE motorhome.motorhome SET " +
							"approved = ?," +
							"Person_idPerson = ?," +
							"Paycard_idPaycard = ?" +
							"WHERE driversLicense = ?");

			preparedStatement.setBoolean(1, thing.isApproved());
			// Sets id of appropriate Person object
			preparedStatement.setInt(2, thing.getPerson().getPersonID());
			// Sets id of appropriate PayCard object
			preparedStatement.setInt(3, thing.getPayCard().getCardID());

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