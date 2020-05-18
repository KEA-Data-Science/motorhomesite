package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Employee;

import java.sql.Connection;
import java.util.List;

public class EmployeeDAO implements IDAO<Employee,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Employee thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Employee read(Integer id)
	{
		return null;
	}

	@Override
	public List<Employee> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Employee thing)
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