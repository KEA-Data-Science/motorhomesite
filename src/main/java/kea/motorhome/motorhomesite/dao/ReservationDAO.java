package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Reservation;

import java.sql.Connection;
import java.util.List;

public class ReservationDAO implements IDAO<Reservation, Integer>
{

    private Connection connection;

    /**
     * @param thing
     */
    @Override
    public boolean create(Reservation thing)
    {
        return false;
    }

    /**
     * @param id
     */
    @Override
    public Reservation read(Integer id)
    {
        return null;
    }

    @Override
    public List<Reservation> readall()
    {
        return null;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Reservation thing)
    {
        return false;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        return false;
    }
}