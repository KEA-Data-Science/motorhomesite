package kea.motorhome.motorhomesite.dao;
// tv, KCN
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PeriodDAO implements IDAO<Period,Integer> {

	/* sql connection to db motorhome */
	private Connection connection;

	public PeriodDAO()
	{
		connection = DBConnectionManager.getConnection(); // singlton instance
	}

	/**
	 * Method writes an entity to period table based on supplied Period-thing.
	 * Returns true if row were written.
	 *
	 * @param thing
	 */
	@Override
	public boolean create(Period thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO motorhome.period (start, end) " +
							"VALUES (?,?)");

			preparedStatement.setDate(1, Date.valueOf(thing.getStart()));
			preparedStatement.setDate(2, Date.valueOf(thing.getEnd()));

			return preparedStatement.executeUpdate() > 0;

		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}
/**
 * Method writes an entity to period table based on supplied Period-thing.
 * Returns the auto-generated row id of the newly written entity in the db.
 *
 * */
	public int create_getID(Period thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO motorhome.period (start, end) " +
					"VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setDate(1, Date.valueOf(thing.getStart()));
			preparedStatement.setDate(2, Date.valueOf(thing.getEnd()));

			preparedStatement.executeUpdate();
			ResultSet resultSet = 	preparedStatement.getGeneratedKeys();

			if(resultSet.next()) // if an id was generated
			{ // resultset has 1 row, with auto-generated key for created entity in db
				return resultSet.getInt(1);
			}

		} catch(SQLException e) { e.printStackTrace(); }

		return -1; // return -1 to signal occurred error
	}
	/* https://www.mysqltutorial.org/mysql-jdbc-insert/ */



	/**
	 * @param id
	 */
	@Override
	public Period read(Integer id)
	{
		Period period = new Period();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM motorhome.period WHERE idPeriod = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) { fillPeriodValuesFromResultSet(period, resultSet); }

			return period;

		} catch(SQLException e) { e.printStackTrace(); }

		return null;
	}

	private void fillPeriodValuesFromResultSet(Period period, ResultSet resultSet) throws SQLException
	{
		period.setPeriodID(resultSet.getInt(1));
		period.setStart(resultSet.getDate(2).toLocalDate());
		period.setEnd(resultSet.getDate(3).toLocalDate());
	}

	@Override
	public List<Period> readall()
	{
		List<Period> periods = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		try
		{
			preparedStatement = connection.prepareStatement(
					"SELECT * FROM motorhome.period");

			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next())
			{
				Period period = new Period();
				fillPeriodValuesFromResultSet(period, resultSet);
				periods.add(period);
			}
		} catch(SQLException e) { e.printStackTrace(); }

		return periods;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Period thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE motorhome.period SET " +
							"start = ?," +
							"end = ? " +
							"WHERE idPeriod = ?");

			preparedStatement.setDate(1, Date.valueOf(thing.getStart()));
			preparedStatement.setDate(2, Date.valueOf(thing.getEnd()));
			preparedStatement.setInt(3, thing.getPeriodID());

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
					"DELETE FROM motorhome.period WHERE idPeriod = ?");
			preparedStatement.setInt(1, id);

			return preparedStatement.executeUpdate() > 0;

		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}
}