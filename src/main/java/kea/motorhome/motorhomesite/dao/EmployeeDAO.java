package kea.motorhome.motorhomesite.dao;
// by TV, kcn
import kea.motorhome.motorhomesite.models.Employee;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Class is a Data Access Object for Employee objects. Find notes for all method 'types' in the
 * DAOs of Reservation, Person, and Service*/
public class EmployeeDAO implements IDAO<Employee,Integer> {

	/* sql connection to db motorhome */
	private Connection connection;

	public EmployeeDAO()
	{
		connection = DBConnectionManager.getConnection(); // singlton instance
	}
	/**
	 * @param thing
	 */
	@Override
	public boolean create(Employee thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO motorhome.employee (accountancyId, Person_idPerson) " +
							"VALUES (?,?)");

			preparedStatement.setInt(1, thing.getAccountancyID());
			preparedStatement.setInt(2, thing.getPerson().getPersonID());

			return preparedStatement.executeUpdate() > 0;

		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Employee read(Integer id)
	{
		Employee employee = new Employee();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM motorhome.employee WHERE idEmployee = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) { fillInEmployeeValues(employee, resultSet); }

			return employee;

		} catch(SQLException e) { e.printStackTrace(); }

		return null;

	}
	private void fillInEmployeeValues(Employee employee, ResultSet resultSet)
	{
		try
		{
			employee.setEmployeeID(resultSet.getInt(1));
			employee.setAccountancyID(resultSet.getInt(2));
			employee.setPerson(SiteDAOCollection.getInstance().personDAO().read(resultSet.getInt(3)));
			// done, nothing to return
		} catch(SQLException e) { e.printStackTrace(); }
	}

	@Override
	public List<Employee> readall()
	{/*List to contain all Employee objects from DB*/
		List<Employee> employees = new ArrayList<>();

		try
		{/* PreparedStatement to create query for db */
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM motorhome.employee");
			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next())
			{
				Employee employee = new Employee();
				fillInEmployeeValues(employee, resultSet); // values attributed
				employees.add(employee); // assembled employee added to returned list
			}
			/* letting exceptions flow for this iteration */
		} catch(SQLException e) { e.printStackTrace(); }
		return employees;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Employee thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE motorhome.employee SET " +
							"accountancyId = ?," +
							"Person_IdPerson = ? " +
							"WHERE idEmployee = ?");
			/*filling in the attribute values from Person object, id last because of 'preppedSt.' */
			preparedStatement.setInt(1, thing.getAccountancyID());
			preparedStatement.setInt(2, thing.getPerson().getPersonID());
			preparedStatement.setInt(3, thing.getEmployeeID());

			/* e.Update returns the num of rows manipulated. */
			int numChanges = preparedStatement.executeUpdate();
			/* if numChanges above zero, DB was written to */
			return numChanges > 0;
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		/* reaching here means things went awry */
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
					"DELETE FROM motorhome.employee WHERE idEmployee = ?");

			preparedStatement.setInt(1, id);
			return preparedStatement.executeUpdate() > 0;

			/* still doing nothing, should really log */
		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}
}