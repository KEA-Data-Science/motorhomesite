package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Customer;

import java.sql.Connection;
import java.util.List;

public class CustomerDAO implements IDAO<Customer,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Customer thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Customer read(Integer id)
	{
		return null;
	}

	@Override
	public List<Customer> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Customer thing)
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