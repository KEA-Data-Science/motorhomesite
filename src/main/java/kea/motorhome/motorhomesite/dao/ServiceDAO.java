package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Service;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO implements IDAO<Service, Integer>
{

    private Connection connection;
    private SiteDAOCollection dao;

    public ServiceDAO()
    {
        connection = DBConnectionManager.getConnection();
        dao = SiteDAOCollection.getInstance();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(Service thing)
    {
        try
        {        /* construction notes; error in DB-model, will not work before db-fix 28/5 */
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.service (name, description, unitPrice) " +
                    "VALUES (?,?,?)");

            preparedStatement.setString(1, thing.getName());
            preparedStatement.setString(1, thing.getName());
            preparedStatement.setFloat(1, thing.getUnitPrice());

            return preparedStatement.executeUpdate() > 0;

        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public Service read(Integer id)
    {
        Service service = new Service();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.service WHERE idService = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                fillServiceValuesFromResultSet(service, resultSet);
            }

            return service;
        } catch(SQLException e) {e.printStackTrace(); }

        return null;
    }

    private void fillServiceValuesFromResultSet(Service service, ResultSet resultSet) throws SQLException
    {
        service.setServiceID(resultSet.getInt(1));
        service.setName(resultSet.getString(2));
        service.setDescription(resultSet.getString(3));
        service.setUnitPrice(resultSet.getInt(4));
    }

    @Override
    public List<Service> readall()
    {
        List<Service> services = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.service");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Service service = new Service();
                fillServiceValuesFromResultSet(service, resultSet);
                services.add(service);
            }
            return services;
        } catch(SQLException e) {e.printStackTrace(); }

        return null;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Service thing)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.service SET " +
                    "name = ?," +
                    "description = ?," +
                    "unitPrice = ?" +
                    "WHERE idService = ?");

            preparedStatement.setString(1, thing.getName());
            preparedStatement.setString(2, thing.getDescription());
            preparedStatement.setFloat(3, thing.getUnitPrice());
            preparedStatement.setInt(4, thing.getServiceID());

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
					"DELETE FROM motorhome.service WHERE idService = ?");
			preparedStatement.setInt(1, id);

			return preparedStatement.executeUpdate() > 0;
		}catch(SQLException e){
    		e.printStackTrace();
		}

        return false;
    }
}