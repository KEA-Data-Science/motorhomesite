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

        /*motorhomes.add(new Motorhome(1,new CarModel("Big One Runnrunn", "UHYTG4334", 120, 3),2018
                ,"This wonderful vehicle can take you anywhere as long as it is not uphill.",
                203.0f,4,"/img/MH1.jpg"));
        motorhomes.add(new Motorhome(2, new CarModel("Busty Deep Seater", "UHYTG4334", 150, 2),2014
                ,"Thompsons Teeth are the Only Teeth that can Chew Other Teeth! Also, this is not a vehicle" +
                ", but a prop useful only for pseudo-pornographic and semi-educational purposes.",
                203.0f,4,"../img/MH2.jpg"));
        motorhomes.add(new Motorhome(3, new CarModel("Normal Mobile Home", "UHYTG4334", 170, 2), 2004
                ,"If you rent this car, nothing in your life will be the same. Literally, you will be " +
                "transported to another Universe without oxygen, and sadly, suffocate. The good news is, " +
                "you won't really be real at that point, because the laws of physics in that Universe do " +
                "not allow matter to subsist.",
                203.0f,4,"img/MH3.jpg"));
        motorhomes.add(new Motorhome(4, new CarModel("Heritage Mobile Home", "UHYTG4334", 170, 2), 1954
                ,"Winston Churchill spent his last four summers traveling around this old baby. She " +
                "traveled the Alps before Tiger Woods. Anyway, the vehicle is a complete deathtrap.",
                203.0f,4,"../img/MH4.jpg"));*/

        motorhomes.add(new Motorhome(1, new CarModel("Big One Runnrunn", "UHYTG4334", 120, 3), "ZT-56-005", new float[]{140.3f, 193.3f, 290f},
                "Notes are meant to please the soul, not cause angaish",
                "/img/MH1.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(2, new CarModel("Busty Deep Seater", "UHYTG4334", 150, 2), "BAD#443", new float[]{120.3f, 193.3f, 290f}, "Notes are meant to please the soul, not cause angaish", "/img/MH2.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(3, new CarModel("Normal Mobile Home", "UHYTG4334", 170, 2), "BAD#442", new float[]{150.3f, 203.3f, 340f}, "Notes are meant to please the soul, not cause angaish", "/img/MH3.jpg", new ArrayList<Service>()));
        motorhomes.add(new Motorhome(4, new CarModel("Heritage Mobile Home", "UHYTG4334", 170, 2), "BAD#441", new float[] {143.3f, 183.3f, 290f}, "Notes are meant to please the soul, not cause angaish"
                , "/img/MH4.jpg", new ArrayList<Service>()));

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
