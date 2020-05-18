package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Motorhome;

import java.sql.Connection;
import java.util.List;

public class MotorhomeDAO implements IDAO<Motorhome,Integer> {

	public MotorhomeDAO(Connection connection)
	{
		this.connection = connection;
	}

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Motorhome thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Motorhome read(Integer id)
	{
		return null;
	}

	@Override
	public List<Motorhome> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Motorhome thing)
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