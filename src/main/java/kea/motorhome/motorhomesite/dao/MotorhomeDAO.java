package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.*;
import kea.motorhome.motorhomesite.util.DBConnectionManager;
import kea.motorhome.motorhomesite.util.Grouper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MotorhomeDAO implements IDAO<Motorhome, Integer>
{
	private Connection connection;
	private SiteDAOCollection dao;

	public MotorhomeDAO()
	{
		connection = DBConnectionManager.getConnection();
		dao = SiteDAOCollection.getInstance();
	}

	// TODO: Tabellen mangler servicesAvailable VARCHAR(200), og seasonalDailyCharge skal ændres til VARCHAR(200)
	// TODO: Efter servicesAvailable er tiføjet til tabellen: Check om indekserne stadig passer
	@Override
	public boolean create(Motorhome thing) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO motorhome.motorhome (licensePlate, notes, imageURL, /* not in table */ servicesAvailable /* not in table */, productionYear, description, minimumDaysOfRental, fuelType, SeasonalDailyCharge, carModel_idcarModel) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?)");

			// CSV String to hold serviceIDs
			String servicesAvailableAsCSVString = "";
			for (int i = 0; i < thing.getServicesAvailable().size(); i++)
			{
				servicesAvailableAsCSVString.concat(thing.getServicesAvailable().get(i).getServiceID() + ",");
			}

			// CSV String to hold 3 element float[]
			String seasonalDailyChargeAsCSVString = thing.getSeasonalDailyCharge()[0] + "," +
					thing.getSeasonalDailyCharge()[1] + "," +
					thing.getSeasonalDailyCharge()[2] + ",";

			preparedStatement.setString(1, thing.getLicensePlate());
			preparedStatement.setString(2, thing.getNotes());
			preparedStatement.setString(3, thing.getImageURL());
			// servicesAvailableAsCSVString represents a List<Service>
			preparedStatement.setString(4, servicesAvailableAsCSVString);
			preparedStatement.setInt(5, thing.getProductionYear());
			preparedStatement.setString(6, thing.getDescription());
			preparedStatement.setInt(7, thing.getMinimumDaysOfRental());
			preparedStatement.setString(8, thing.getFuelType());
			// seasonalDailyChargesAsCSVString represents a float array
			preparedStatement.setString(9, seasonalDailyChargeAsCSVString);
			preparedStatement.setInt(10, thing.getModel().getCarModelID());

			return preparedStatement.executeUpdate() > 0;
		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}

	private void fillMotorhomeValuesFromResultSet(Motorhome motorhome, ResultSet resultSet) throws SQLException {
		motorhome.setMotorhomeID(resultSet.getInt(1));
		motorhome.setLicensePlate(resultSet.getString(2));
		motorhome.setNotes(resultSet.getString(3));
		motorhome.setImageURL(resultSet.getString(4));
		// List of every services at all
		ArrayList<Service> everyService = (ArrayList<Service>) dao.serviceDAO().readall();
		// List of serviceIDs from CSV String
		ArrayList<Integer> listOfServiceIDs = Grouper.splitCSVString_IntList(resultSet.getString(5),",",-1,true);
		// List of available services taken from their ids
		ArrayList<Service> servicesFromListOfServiceIDs = new ArrayList<>();
		for (int i = 0; i < listOfServiceIDs.size(); i++) {
			servicesFromListOfServiceIDs.add(everyService.get(listOfServiceIDs.get(i)));
		}
		motorhome.setServicesAvailable(servicesFromListOfServiceIDs);
		motorhome.setProductionYear(resultSet.getInt(6));
		motorhome.setDescription(resultSet.getString(7));
		motorhome.setMinimumDaysOfRental(resultSet.getInt(8));
		motorhome.setFuelType(resultSet.getString(9));
		// List of 3 Strings (representing floats) from CSV String
		ArrayList<String> numbersFromCSVString = Grouper.splitStringAsCSV(resultSet.getString(10),",",-1);
		// Float array to set as seasonDailyCharges, values from numbersFromCSVString
		float floatArrayFromListOfStrings[] = {
				Float.parseFloat(numbersFromCSVString.get(0)),
				Float.parseFloat(numbersFromCSVString.get(1)),
				Float.parseFloat(numbersFromCSVString.get(2)),
		};
		motorhome.setSeasonalDailyCharge(floatArrayFromListOfStrings);
		// Set CarModel with corresponding carModelID
		motorhome.setModel(dao.carModelDAO().read(resultSet.getInt(11)));
	}

	@Override
	public Motorhome read(Integer id)
	{
		Motorhome motorhome = new Motorhome();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM motorhome.motorhome WHERE idMotorhome = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) { fillMotorhomeValuesFromResultSet(motorhome, resultSet); }

			return motorhome;

		} catch(SQLException e) { e.printStackTrace(); }

		return null;
	}


	@Override
	public List<Motorhome> readall()
	{
		List<Motorhome> motorhomes = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		try
		{
			preparedStatement = connection.prepareStatement(
					"SELECT * FROM motorhome.motorhome");

			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next())
			{
				Motorhome motorhome = new Motorhome();
				fillMotorhomeValuesFromResultSet(motorhome, resultSet);
				motorhomes.add(motorhome);
			}
		} catch(SQLException e) { e.printStackTrace(); }

		return motorhomes;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Motorhome thing)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE motorhome.motorhome SET " +
							"licensePlate = ?," +
							"notes = ?," +
							"imageURL = ?," +
							"servicesAvailable = ?" +
							"productionYear = ?" +
							"description = ?" +
							"minimumDaysOfRental = ?" +
							"fuelType = ?" +
							"seasonalDailyCharges = ?" +
							"carModel_idcarModel = ?" +
							"WHERE idMotorhome = ?");

			preparedStatement.setString(1, thing.getLicensePlate());
			preparedStatement.setString(2, thing.getNotes());
			preparedStatement.setString(3, thing.getImageURL());
			// CSV String to hold serviceIDs
			String servicesAvailableAsCSVString = "";
			for (int i = 0; i < thing.getServicesAvailable().size(); i++)
			{
				servicesAvailableAsCSVString.concat(thing.getServicesAvailable().get(i).getServiceID() + ",");
			}
			preparedStatement.setString(4, servicesAvailableAsCSVString);
			preparedStatement.setInt(5, thing.getProductionYear());
			preparedStatement.setString(6, thing.getDescription());
			preparedStatement.setInt(7, thing.getMinimumDaysOfRental());
			preparedStatement.setString(8, thing.getFuelType());
			// CSV String to hold 3 element float[]
			String seasonalDailyChargeAsCSVString = thing.getSeasonalDailyCharge()[0] + "," +
					thing.getSeasonalDailyCharge()[1] + "," +
					thing.getSeasonalDailyCharge()[2] + ",";
			preparedStatement.setString(9, seasonalDailyChargeAsCSVString);
			preparedStatement.setInt(10, thing.getModel().getCarModelID());

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
					"DELETE FROM motorhome.motorhome WHERE idMotorhome = ?");
			preparedStatement.setInt(1, id);

			return preparedStatement.executeUpdate() > 0;

		} catch(SQLException e) { e.printStackTrace(); }

		return false;
	}
}