package kea.motorhome.motorhomesite.dao;

import java.util.List;

public interface IDAO<T,U> {

	/**
	 * Creates a new entity in data-source based on the supplied thing.
	 * Returns true if successful.
	 */
	boolean create(T thing);

	 /**
	 * Returns a T-type object, read from data-source
	  * - queried with supplied id (type U).
	 * Returns null if there is no 'thing' by id in db.
	 */
	T read(U id);

	/**
	 * Method queries data-source and returns complete list of paralleled
	 * object of type T wrapped in a List
	*/
	List<T> readall();

	/**
	 * Method executes update to DB based on supplied thing type-T:
	 * Returns true if update was written to DB, false if nothing was written.
	 */
	boolean update(T thing);

	/**
	 * Method removes object from data-source, where data-source entity-id
	 * equals the supplied id, and returns a thing of type U
	 */
	boolean delete(U id);

}