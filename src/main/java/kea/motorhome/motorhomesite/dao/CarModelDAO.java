package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.CarModel;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarModelDAO implements IDAO<CarModel, Integer> {

    private Connection connection;
    private SiteDAOCollection dao;

    public CarModelDAO()
    {
        connection = DBConnectionManager.getConnection();
        dao = SiteDAOCollection.getInstance();
    }

    @Override
    public boolean create(CarModel thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.carModel (modelName, modelNumber, horsePower, beds, engineCapacity, length, height, width, weight, hotWaterCapacity, coldWaterCapacity, numberOfSeats, oven, cruiseControl, shower) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, thing.getModelName());
            preparedStatement.setString(2, thing.getModelnumber());
            preparedStatement.setInt(3, thing.getHorsePower());
            preparedStatement.setInt(4, thing.getBeds());
            preparedStatement.setFloat(5, thing.getEngineCapacity());
            preparedStatement.setFloat(6, thing.getLength());
            preparedStatement.setFloat(7, thing.getHeight());
            preparedStatement.setFloat(8, thing.getWidth());
            preparedStatement.setFloat(9, thing.getWeight());
            preparedStatement.setFloat(10, thing.getHotWaterCapacity());
            preparedStatement.setFloat(11, thing.getColdWaterCapacity());
            preparedStatement.setInt(12, thing.getNumberOfSeats());
            preparedStatement.setBoolean(13, thing.isOven());
            preparedStatement.setBoolean(14, thing.isCruiseControl());
            preparedStatement.setBoolean(15, thing.isShower());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    private void fillCarModelValuesFromResultSet(CarModel carModel, ResultSet resultSet) throws SQLException
    {
        carModel.setCarModelID(resultSet.getInt(1));
        carModel.setModelName(resultSet.getString(2));
        carModel.setModelnumber(resultSet.getString(3));
        carModel.setHorsePower(resultSet.getInt(4));
        carModel.setBeds(resultSet.getInt(5));
        carModel.setEngineCapacity(resultSet.getFloat(6));
        carModel.setLength(resultSet.getFloat(7));
        carModel.setHeight(resultSet.getFloat(8));
        carModel.setWidth(resultSet.getFloat(9));
        carModel.setWeight(resultSet.getFloat(10));
        carModel.setHotWaterCapacity(resultSet.getFloat(11));
        carModel.setColdWaterCapacity(resultSet.getFloat(12));
        carModel.setNumberOfSeats(resultSet.getInt(13));
        carModel.setOven(resultSet.getBoolean(14));
        carModel.setCruiseControl(resultSet.getBoolean(15));
        carModel.setShower(resultSet.getBoolean(16));
    }

    @Override
    public CarModel read(Integer id)
    {
        CarModel carModel = new CarModel();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.carModel WHERE idcarModel = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) { fillCarModelValuesFromResultSet(carModel, resultSet); }

            return carModel;

        } catch(SQLException e) { e.printStackTrace(); }

        return null;
    }

    @Override
    public List<CarModel> readall()
    {
        List<CarModel> carModels = new ArrayList<>();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.carModel");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                CarModel carModel = new CarModel();
                fillCarModelValuesFromResultSet(carModel, resultSet);
                carModels.add(carModel);
            }
        } catch(SQLException e) { e.printStackTrace(); }

        return carModels;
    }

    @Override
    public boolean update(CarModel thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.carModel SET " +
                            "modelName = ?," +
                            "modelNumber = ?," +
                            "horsePower = ?," +
                            "beds = ?," +
                            "enginecapacity = ?" +
                            "length = ?" +
                            "height = ?" +
                            "width = ?" +
                            "weight = ?" +
                            "hotWaterCapacity = ?" +
                            "coldWaterCapacity = ?" +
                            "numberOfSeats = ?" +
                            "oven = ?" +
                            "cruiseControl = ?" +
                            "shower = ?" +
                            "WHERE idcarModel = ?");

            preparedStatement.setString(1, thing.getModelName());
            preparedStatement.setString(2, thing.getModelnumber());
            preparedStatement.setInt(3, thing.getHorsePower());
            preparedStatement.setInt(4, thing.getBeds());
            preparedStatement.setFloat(5, thing.getEngineCapacity());
            preparedStatement.setFloat(6, thing.getLength());
            preparedStatement.setFloat(7, thing.getHeight());
            preparedStatement.setFloat(8, thing.getWidth());
            preparedStatement.setFloat(9, thing.getWeight());
            preparedStatement.setFloat(10, thing.getHotWaterCapacity());
            preparedStatement.setFloat(11, thing.getColdWaterCapacity());
            preparedStatement.setInt(12, thing.getNumberOfSeats());
            preparedStatement.setBoolean(13, thing.isOven());
            preparedStatement.setBoolean(14, thing.isCruiseControl());
            preparedStatement.setBoolean(15, thing.isShower());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    public boolean delete(Integer id)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.carModel WHERE idcarModel = ?");
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}


