package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.CarModel;
import kea.motorhome.motorhomesite.models.Motorhome;
import kea.motorhome.motorhomesite.models.Service;

import java.util.ArrayList;
import java.util.List;

public class MotorhomeDAODemo implements IDAO<Motorhome, Integer>
{
    List<Motorhome> motorhomes;

    public MotorhomeDAODemo()
    {
        motorhomes = new ArrayList<>();
    }

    @Override
    public boolean create(Motorhome thing)
    {
        return motorhomes.add(thing);
    }

    @Override
    public List<Motorhome> readall()
    {
        return motorhomes;
    }

    public Motorhome read(Integer id)
    {
        for(Motorhome motorhome : motorhomes)
        {
            if(motorhome.getMotorhomeID() == id)
                return motorhome;
        }
        return null;
    }

    @Override
    public boolean update(Motorhome motorhome)
    {
        for(int i = 0; i < motorhomes.size(); i++)
        {
            if(motorhomes.get(i).getMotorhomeID() == motorhome.getMotorhomeID())
            {
                motorhomes.set(i, motorhome);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id)
    {
        for(Motorhome motorhome : motorhomes)
        {
            if(id == motorhome.getMotorhomeID())
                return motorhomes.remove(motorhome);
        }
        return false;
    }
}
