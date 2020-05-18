package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Service;

import java.sql.Connection;
import java.util.List;

public class ServiceDAO implements IDAO<Service,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Service thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Service read(Integer id)
	{
		return null;
	}

	@Override
	public List<Service> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Service thing)
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