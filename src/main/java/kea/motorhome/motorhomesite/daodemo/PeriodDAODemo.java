package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Period;

import java.util.ArrayList;
import java.util.List;

public class PeriodDAODemo implements IDAO<Period, Integer>
{
    ArrayList<Period> periods;

    public PeriodDAODemo()
    {
        periods = new ArrayList<>();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(Period thing)
    {
        return periods.add(thing);
    }

    /**
     * @param id
     */
    @Override
    public Period read(Integer id)
    {
        for(Period period : periods)
        {
            if(period.getPeriodID() == id){return period;}
        }

        return null;
    }

    @Override
    public List<Period> readall()
    {
        return periods;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Period thing)
    {
        for(int i = 0; i < periods.size();i++)
        {
            if(periods.get(i).getPeriodID() == thing.getPeriodID()){periods.set(i, thing);}
            return true;
        }


        return false;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        for(int i = 0; i < periods.size();i++)
        {
            if(periods.get(i).getPeriodID() == id){periods.remove(i);
            return true;}
        }

        return false;
    }
}
