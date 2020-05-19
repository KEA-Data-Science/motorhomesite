package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
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

        motorhomes.add(new Motorhome(1, "Chrysler Faiz", "BAD#444", new float[]{23.3f, 23.3f, 0f, 0f}, "Notes are" +
                                                                                                       " meant to" +
                                                                                                       " please " +
                                                                                                       "the soul," +
                                                                                                       " not " +
                                                                                                       "cause " +
                                                                                                       "angaish",
                                     "/img/MH1.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(2, "Ponzeyer Faiz", "BAD#443", new float[]{23.3f, 23.3f, 0f, 0f}, "NA", "/img/MH2.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(3, "Gadleler Faiz", "BAD#442", new float[]{23.3f, 23.3f, 0f, 0f}, "NA", "/img/MH3.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(4, "R'ysler Faiz", "BAD#441", new float[]{23.3f, 23.3f, 0f, 0f}, "NA", "/img/MH4.jpg", new ArrayList<Service>()));
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
