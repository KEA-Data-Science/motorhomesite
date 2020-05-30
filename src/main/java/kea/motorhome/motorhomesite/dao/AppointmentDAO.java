package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.Appointment;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements IDAO<Appointment,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(Appointment thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public Appointment read(Integer id)
	{
		return null;
	}

	@Override
	public List<Appointment> readall()
	{

		return new ArrayList<>(); // proxy until someone writes this
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(Appointment thing)
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