package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.CarModel;
import kea.motorhome.motorhomesite.util.DBConnectionManager;
import org.springframework.objenesis.SpringObjenesis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarModelDAO implements IDAO<CarModel, Integer>
{
    private Connection connection;
    private SiteDAOCollection dao;

    public CarModelDAO()
    {
        connection = DBConnectionManager.getConnection();
        dao = SiteDAOCollection.getInstance();
    }

    /**
     * Creates a new entity in data-source based on the supplied thing.
     * Returns true if successful.
     *
     * @param thing
     */
    @Override
    public boolean create(CarModel thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.carmodel (" +
                    "modelName, " +
                    "modelnumber, " +
                    "horsePower, " +
                    "beds, " +
                    "engineCapacity, " +
                    "length," +
                    "width," +
                    "height," +
                    "weight," +
                    "hotWaterCapacity," +
                    "coldWaterCapacity," +
                    "numberOfSeats," +
                    "oven," +
                    "cruiseControl," +
                    "shower) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            preparedStatement.setString(1, thing.getModelName());
            preparedStatement.setString(2, thing.getModelnumber());
            preparedStatement.setInt(3, thing.getHorsePower());
            preparedStatement.setInt(4, thing.getBeds());
            preparedStatement.setFloat(5, thing.getEngineCapacity());
            preparedStatement.setFloat(6, thing.getLength());
            preparedStatement.setFloat(7, thing.getWidth());
            preparedStatement.setFloat(8, thing.getHeight());
            preparedStatement.setFloat(9, thing.getWeight());
            preparedStatement.setFloat(10, thing.getHotWaterCapacity());
            preparedStatement.setFloat(11, thing.getColdWaterCapacity());
            preparedStatement.setInt(12, thing.getNumberOfSeats());
            preparedStatement.setByte(13, (byte)(!thing.isOven() ? 0 : 1)); // converting bool to byte
            preparedStatement.setByte(14, (byte)(!thing.isCruiseControl() ? 0 : 1));
            preparedStatement.setByte(15, (byte)(!thing.isShower() ? 0 : 1));

            return preparedStatement.executeUpdate() > 0;
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a T-type object, read from data-source
     * - queried with supplied id (type U).
     * Returns null if there is no 'thing' by id in db.
     *
     * @param id
     */
    @Override
    public CarModel read(Integer id)
    {
        CarModel model = new CarModel();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.carmodel WHERE idcarModel = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {fillCarModelValuesFromResultSet(model, resultSet);}

            return model;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    private void fillCarModelValuesFromResultSet(CarModel model, ResultSet resultSet) throws SQLException
    {
        model.setCarModelID(resultSet.getInt(1));
        model.setModelName(resultSet.getString(2));
        model.setModelnumber(resultSet.getString(3));
        model.setHorsePower(resultSet.getInt(4));
        model.setBeds(resultSet.getInt(5));
        model.setEngineCapacity(resultSet.getFloat(6));
        model.setLength(resultSet.getFloat(7));
        model.setWidth(resultSet.getFloat(8));
        model.setHeight(resultSet.getFloat(9));
        model.setWeight(resultSet.getFloat(10));
        model.setHotWaterCapacity(resultSet.getFloat(11));
        model.setColdWaterCapacity(resultSet.getFloat(12));
        model.setNumberOfSeats(resultSet.getInt(13));
        model.setOven(resultSet.getByte(14) != 0);
        model.setCruiseControl(resultSet.getByte(15) != 0);
        model.setShower(resultSet.getByte(16) != 0);
    }

    /**
     * Method queries data-source and returns complete list of paralleled
     * object of type T wrapped in a List
     */
    @Override
    public List<CarModel> readall()
    {
        List<CarModel> carModels = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.carmodel");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                CarModel model = new CarModel();
                fillCarModelValuesFromResultSet(model, resultSet);
                carModels.add(model);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return carModels;
    }

    /**
     * Method executes update to DB based on supplied thing type-T:
     * Returns true if update was written to DB, false if nothing was written.
     *
     * @param thing
     */
    @Override
    public boolean update(CarModel thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.carmodel SET " +
                    "modelName = ?," +
                    "modelnumber = ?," +
                    "horsePower = ?," +
                    "beds = ?," +
                    "engineCapacity = ?," +
                    "length = ?," +
                    "width = ?," +
                    "height = ?," +
                    "weight = ?," +
                    "hotWaterCapacity = ?," +
                    "coldWaterCapacity = ?," +
                    "numberOfSeats = ?," +
                    "oven = ?," +
                    "cruiseControl = ?," +
                    "shower = ? WHERE idcarModel = ?");

            preparedStatement.setString(1,thing.getModelName());
            preparedStatement.setString(2,thing.getModelnumber());
            preparedStatement.setInt(3,thing.getHorsePower());
            preparedStatement.setInt(4, thing.getBeds());
            preparedStatement.setFloat(5,thing.getEngineCapacity());
            preparedStatement.setFloat(5,thing.getLength());
            preparedStatement.setFloat(6,thing.getWidth());
            preparedStatement.setFloat(7,thing.getHeight());
            preparedStatement.setFloat(8,thing.getWeight());
            preparedStatement.setFloat(8,thing.getHotWaterCapacity());
            preparedStatement.setFloat(8,thing.getColdWaterCapacity());
            preparedStatement.setInt(9,thing.getNumberOfSeats());
            preparedStatement.setByte(10, (byte)(thing.isOven()?1:0));
            preparedStatement.setByte(11, (byte)(thing.isCruiseControl()?1:0));
            preparedStatement.setByte(12, (byte)(thing.isShower()?1:0));

        } catch()

        return false;
    }

    /**
     * Method removes object from data-source, where data-source entity-id
     * equals the supplied id, and returns a thing of type U
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        return false;
    }
}
