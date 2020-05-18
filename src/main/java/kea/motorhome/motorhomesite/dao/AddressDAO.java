package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Address;

import java.sql.Connection;
import java.util.List;

public class AddressDAO implements IDAO<Address,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Address thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Address read(Integer id)
	{
		return null;
	}

	@Override
	public List<Address> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Address thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public boolean delete(Integer id)
	{
		return false;
	}
}