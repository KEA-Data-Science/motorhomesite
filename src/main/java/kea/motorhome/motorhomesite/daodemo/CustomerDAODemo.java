package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAODemo implements IDAO<Customer, String> {

        List<Customer> customers;

        public CustomerDAODemo()
        {
            customers = new ArrayList<>();
        }
        @Override
        public boolean create(Customer thing) {
            return customers.add(thing);
        }

        @Override
        public List<Customer> readall()
        {
            return customers;
        }

        public Customer read(String id)
        {
            for (Customer customer : customers)
                if (customer.getDriversLicence().equals(id))
                    return customer;
            return null;
        }

        @Override
        public boolean update(Customer customer)
        {
            for (int i = 0; i < customers.size(); i++)
            {
                if (customers.get(i).getDriversLicence().equals(customer.getDriversLicence())) {
                    customers.set(i, customer);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean delete(String id) {
            for (Customer customer : customers)
            {
                if (customer.getDriversLicence().equals(id))
                {
                    customers.remove(customer);
                    return true;
                }
            }
            return false;
        }

}
