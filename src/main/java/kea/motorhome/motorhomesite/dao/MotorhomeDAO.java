package kea.motorhome.motorhomesite.dao;
// by KCN, LNS
import kea.motorhome.motorhomesite.models.*;
import kea.motorhome.motorhomesite.util.DBConnectionManager;
import kea.motorhome.motorhomesite.util.Grouper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/** Class acts as a Data Access Object, facilitating contact between the
 * data-layer and business logic.*/
public class MotorhomeDAO implements IDAO<Motorhome, Integer>
{
    private Connection connection;


    public MotorhomeDAO()
    {
        connection = DBConnectionManager.getConnection();
    }

    @Override
    public boolean create(Motorhome thing)
    {
        try
        {
            PreparedStatement preparedStatement = preparedStatementToWriteRowToMotorhomeTable(thing);

            if(preparedStatement.executeUpdate() > 0) // if rows were written

            {    /* if rows were written, get generated key from ResultSet */
                ResultSet generatedKeyResultset = preparedStatement.getGeneratedKeys();
                if(generatedKeyResultset.next())

                {    /* write rows to junction table Motorhome_has_Service with new key */
                    int newMotorhomeID = generatedKeyResultset.getInt(1);

                    writeRowsToMotorhome_has_ServiceTable(thing.getServicesAvailable(), newMotorhomeID);
                }
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    private PreparedStatement preparedStatementToWriteRowToMotorhomeTable(Motorhome thing) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO motorhome.motorhome" +
                "(licensePlate," +
                " notes," +
                " imageURL," +
                " productionYear, " +
                " description," +
                " minimumDaysOfRental," +
                " fuelType," +
                " seasonalDailyCharges," +
                " carModel_idcarModel) " +
                "VALUES (?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, thing.getLicensePlate());
        preparedStatement.setString(2, thing.getNotes());
        preparedStatement.setString(3, thing.getImageURL());
        preparedStatement.setInt(4, thing.getProductionYear());
        preparedStatement.setString(5, thing.getDescription());
        preparedStatement.setInt(6, thing.getMinimumDaysOfRental());
        preparedStatement.setString(7, thing.getFuelType());
        preparedStatement.setFloat(8, thing.getSeasonDailyChargeLowSeason());
        preparedStatement.setInt(9, thing.getModel().getCarModelID());

        return preparedStatement;
    }

    private void writeRowsToMotorhome_has_ServiceTable(List<Service> services, int motorhomeID) throws
                                                                                                SQLException
    {
        for(Service service : services)
        {
            PreparedStatement reservationsServicesStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.motorhome_has_service " +
                    "(Service_idService, Motorhome_idMotorhome) VALUES (?,?)");
            reservationsServicesStatement.setInt(1, service.getServiceID());
            reservationsServicesStatement.setInt(2, motorhomeID);
            reservationsServicesStatement.executeUpdate();
        }
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

            if(resultSet.next()){ reconstructMotorhomeValuesFromResultSet(motorhome, resultSet); }

            return motorhome;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    private void reconstructMotorhomeValuesFromResultSet(Motorhome motorhome, ResultSet resultSet) throws
                                                                                                   SQLException
    {
        motorhome.setMotorhomeID(resultSet.getInt(1));
        motorhome.setLicensePlate(resultSet.getString(2));
        motorhome.setNotes(resultSet.getString(3));
        motorhome.setImageURL(resultSet.getString(4));
        motorhome.setProductionYear(resultSet.getInt(5));
        motorhome.setDescription(resultSet.getString(6));
        motorhome.setMinimumDaysOfRental(resultSet.getInt(7));
        motorhome.setFuelType(resultSet.getString(8));
        float[] pricesArray = new float[]{resultSet.getFloat(9), 0, 0};
        motorhome.setSeasonalDailyCharge(pricesArray); // only first field read; remainders are calculated
        // Set CarModel with corresponding carModelID
        motorhome.setModel(SiteDAOCollection.getInstance().carModelDAO().read(resultSet.getInt(10)));

        motorhome.setServicesAvailable(reconstructServicesListForMotorhome(motorhome.getMotorhomeID()));

    }

    private List<Service> reconstructServicesListForMotorhome(int motorhomeID)
    {
        List<Service> services = new ArrayList<>();

		try
		{
        PreparedStatement serviceAssWithMotorhomeID = connection.prepareStatement(
                "SELECT s.idService, " +
                "	s.name, " +
                "	s.description, " +
                "	s.unitPrice " +
                "	FROM motorhome.motorhome_has_service mhs " +
                "	JOIN motorhome.service s " +
                "	ON mhs.Service_idService = s.idService " +
                "	WHERE mhs.Motorhome_idMotorhome = ?;");
        serviceAssWithMotorhomeID.setInt(1, motorhomeID);

        ResultSet serviceRowsResultSet = serviceAssWithMotorhomeID.executeQuery();

            while(serviceRowsResultSet.next())
            {
                Service service = new Service();
                service.setServiceID(serviceRowsResultSet.getInt(1));
                service.setName(serviceRowsResultSet.getString(2));
                service.setDescription(serviceRowsResultSet.getString(3));
                service.setUnitPrice(serviceRowsResultSet.getFloat(4));
                services.add(service);
            }
        } catch(SQLException e) {e.printStackTrace();}

        return services;
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
                reconstructMotorhomeValuesFromResultSet(motorhome, resultSet);
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
                    "productionYear = ?," +
                    "description = ?," +
                    "minimumDaysOfRental = ?," +
                    "fuelType = ?," +
                    "seasonalDailyCharges = ?" +
//                    "carModel_idcarModel = ?" +
                    "WHERE idMotorhome = ?");

            preparedStatement.setString(1, thing.getLicensePlate());
            preparedStatement.setString(2, thing.getNotes());
            preparedStatement.setString(3, thing.getImageURL());
            preparedStatement.setInt(4, thing.getProductionYear());
            preparedStatement.setString(5, thing.getDescription());
            preparedStatement.setInt(6, thing.getMinimumDaysOfRental());
            preparedStatement.setString(7, thing.getFuelType());
            preparedStatement.setFloat(8, thing.getSeasonDailyChargeLowSeason());
//            preparedStatement.setInt(9, thing.getModel().getCarModelID());
            preparedStatement.setInt(9,thing.getMotorhomeID());

            /* refreshing junction table connections, by first removing all previous junction-entries*/
            removeEntriesFromMotorhomeServiceJunction(thing.getMotorhomeID());
            /* and adding the entries from supplied motorhome; the possibilities of changes between updates
            * are chaotic, and this way is safe. I think.*/
            writeRowsToMotorhome_has_ServiceTable(thing.getServicesAvailable(),thing.getMotorhomeID());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    private void removeEntriesFromMotorhomeServiceJunction(int motorhomeID) throws SQLException
    {
        PreparedStatement deleteAssServicesStatement = connection.prepareStatement(
                "DELETE FROM motorhome.motorhome_has_service" +
                " WHERE Motorhome_idMotorhome = ?");
        deleteAssServicesStatement.setInt(1, motorhomeID);
        deleteAssServicesStatement.executeUpdate();
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

            if(preparedStatement.executeUpdate() > 0) // motorhome was deleted from db
            {
                removeEntriesFromMotorhomeServiceJunction(id);
                return true;
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}