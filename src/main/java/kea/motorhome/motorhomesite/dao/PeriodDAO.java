package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Period;

import java.sql.Connection;
import java.util.List;


public class PeriodDAO implements IDAO<Period,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Period thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Period read(Integer id)
	{
		return null;
	}

	@Override
	public List<Period> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Period thing)
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