package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Reservation;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ReservationDAO implements IDAO<Reservation, Integer>
{
    Connection connection;

    public ReservationDAO(){
        connection = DBConnectionManager.getConnection();
    }

    /**
     * Creates a new entity in data-source based on the supplied thing.
     * Returns true if successful.
     *
     * @param thing
     */
    @Override
    public boolean create(Reservation thing)
    {
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
    public Reservation read(Integer id)
    {
        return null;
    }

    /**
     * Method queries data-source and returns complete list of paralleled
     * object of type T wrapped in a List
     */
    @Override
    public List<Reservation> readall()
    {
        return null;
    }

    /**
     * Method executes update to DB based on supplied thing type-T:
     * Returns true if update was written to DB, false if nothing was written.
     *
     * @param thing
     */
    @Override
    public boolean update(Reservation thing)
    {
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