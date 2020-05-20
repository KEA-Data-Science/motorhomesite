package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationDAODemo implements IDAO<Reservation, Integer>
{
    ArrayList<Reservation> reservations;

    public ReservationDAODemo()
    {
        reservations = new ArrayList<>();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(Reservation thing)
    {
        return reservations.add(thing);
    }

    /**
     * @param id
     */
    @Override
    public Reservation read(Integer id)
    {
        for(int i = 0; i < reservations.size(); i++)
        {
            if(reservations.get(i).getReservationID() == id){return reservations.get(i);}
        }

        return null;
    }

    @Override
    public List<Reservation> readall()
    {
        return reservations;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Reservation thing)
    {
        for(int i = 0; i < reservations.size(); i++)
        {
            if(reservations.get(i).getReservationID() == thing.getReservationID())
            {
                reservations.set(i, thing);
                return true;
            }
        }

        return false;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        for(int i = 0; i < reservations.size(); i++)
        {
            if(reservations.get(i).getReservationID() == id)
            {
                reservations.remove(i);
                return true;
            }
        }


        return false;
    }
}
