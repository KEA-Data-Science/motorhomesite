package kea.motorhome.motorhomesite.dao;


import kea.motorhome.motorhomesite.models.Person;

import java.sql.Connection;
import java.util.List;

public class PersonDAO implements IDAO<Person,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Person thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Person read(Integer id)
	{
		return null;
	}

	@Override
	public List<Person> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Person thing)
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