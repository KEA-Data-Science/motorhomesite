package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.models.PayCard;

import java.sql.Connection;
import java.util.List;

public class PayCardDAO implements IDAO<PayCard,Integer> {

	private Connection connection;

	/**
	 * @param thing
	 */
	@Override
	public boolean create(PayCard thing)
	{
		return false;
	}

	/**
	 * @param id
	 */
	@Override
	public PayCard read(Integer id)
	{
		return null;
	}

	@Override
	public List<PayCard> readall()
	{
		return null;
	}

	/**
	 * @param thing
	 */
	@Override
	public boolean update(PayCard thing)
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