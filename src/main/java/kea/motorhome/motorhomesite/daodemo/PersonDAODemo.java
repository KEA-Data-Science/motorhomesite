package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDAODemo implements IDAO<Person, Integer>
{
    ArrayList<Person> persons;

    /**
     * @param thing
     */
    @Override
    public boolean create(Person thing)
    {
        return persons.add(thing);
    }

    /**
     * @param id
     */
    @Override
    public Person read(Integer id)
    {
        for(int i = 0; i < persons.size(); i++)
        {
            if(persons.get(i).getPersonID() == id){return persons.get(i);}
        }
        return null;
    }

    @Override
    public List<Person> readall()
    {
        return persons;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Person thing)
    {
        for(int i = 0; i < persons.size(); i++)
        {
            if(persons.get(i).getPersonID() == thing.getPersonID())
            {
                persons.set(i, thing);
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
        for(int i = 0; i < persons.size(); i++)
        {
            if(persons.get(i).getPersonID() == id)
            {
                persons.remove(i);
                return true;
            }
        }

        return false;
    }
}
