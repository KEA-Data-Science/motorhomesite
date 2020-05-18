package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Appointment;
import kea.motorhome.motorhomesite.models.Motorhome;

import java.util.ArrayList;
import java.util.List;

public class AddressDAODemo implements IDAO<Address, Integer> {


    List<Address> addresses;

    public AddressDAODemo()
    {
        addresses = new ArrayList<>();
    }
    @Override
    public boolean create(Address thing) {
        return addresses.add(thing);
    }

    @Override
    public List<Address> readall()
    {
        return addresses;
    }

    public Address read(Integer id)
    {
        for (Address address : addresses)
            if (address.getAddressID() == id)
                return address;
        return null;
    }

    @Override
    public boolean update(Address address)
    {
        for (int i = 0; i < addresses.size(); i++)
        {
            if (addresses.get(i).getAddressID() == address.getAddressID()) {
                addresses.set(i, address);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (Address address : addresses)
        {
            if (id == address.getAddressID())
                return addresses.remove(address);
        }
        return false;
    }
}
