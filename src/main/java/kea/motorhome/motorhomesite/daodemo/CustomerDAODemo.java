package kea.motorhome.motorhomesite.daodemo;
// by TV, LNS
import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Customer;
import kea.motorhome.motorhomesite.models.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAODemo implements IDAO<Customer, String>
{

    List<Customer> customers;

    public CustomerDAODemo()
    {
        customers = new ArrayList<>();
    }

    @Override
    public boolean create(Customer thing){ return customers.add(thing); }

    @Override
    public List<Customer> readall()
    {
        return customers;
    }

    @Override
    public Customer read(String id)
    {
        for(Customer customer : customers)
        {   /// KIAN: har taget friheden til at ændre equals til contentEquals
            if(customer.getDriversLicence().contentEquals(id))
                return customer;
        }
        return null;
    }

    @Override
    public boolean update(Customer customer)
    {
        for(int i = 0; i < customers.size(); i++)
        {
            if(customers.get(i).getDriversLicence().equals(customer.getDriversLicence()))
            {
                customers.set(i, customer);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id)
    {
        for(Customer customer : customers)
        {
            if(customer.getDriversLicence().equals(id))
            {
                customers.remove(customer);
                return true;
            }
        }
        return false;
    }

}
