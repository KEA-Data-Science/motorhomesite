package kea.motorhome.motorhomesite.dao;

import java.util.List;

public interface IDAO<T,U> {

	/**
	 * 
	 * @param T
	 */
	boolean create(T thing);

	/**
	 * 
	 * @param U
	 */
	T read(U id);

	List<T> readall();

	/**
	 * 
	 * @param T
	 */
	boolean update(T thing);

	/**
	 * 
	 * @param U
	 */
	boolean delete(U id);

}