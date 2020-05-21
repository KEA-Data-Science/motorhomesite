package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAODemo implements IDAO<Service, Integer>
{
    ArrayList<Service> services;

    public ServiceDAODemo()
    {
        services = new ArrayList<>();
    }

    /**
     * @param thing
     */
    @Override
    public boolean create(Service thing)
    {
        return services.add(thing);
    }

    /**
     * @param id
     */
    @Override
    public Service read(Integer id)
    {
        for(int i = 0; i < services.size(); i++)
        {
            if(services.get(i).getServiceID() == id)
            {
                return services.get(i);
            }
        }

        return null;
    }

    @Override
    public List<Service> readall()
    {
        return services;
    }

    /**
     * @param thing
     */
    @Override
    public boolean update(Service thing)
    {
        for(int i = 0; i < services.size(); i++)
        {
            if(thing.getServiceID() == services.get(i).getServiceID())
            {
                services.set(i,thing);
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
        for(int i = 0; i < services.size(); i++)
        {
            if(id == services.get(i).getServiceID())
            {
                services.remove(i);
                return true;
            }
        }

        return false;
    }

//    public void addServices()
//    {
//        Service service1 = new Service(0, "Cleaning", 20, "Cleaning");
//        Service service2 = new Service(1, "Repairing", 100, "Repairing");
//        Service service3 = new Service(2, "Extra space", 45, "Extra space");
//        services.add(service1);
//        services.add(service2);
//        services.add(service3);
//    }
}
