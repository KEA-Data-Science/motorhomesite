package kea.motorhome.motorhomesite.dao;
// kcn
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.Person;
import kea.motorhome.motorhomesite.util.DBConnectionManager;
import kea.motorhome.motorhomesite.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO implements IDAO<Person, Integer>
{

    private Connection connection;

    public PersonDAO()
    {
        connection = DBConnectionManager.getConnection(); // fetching Singleton instance
    }

    /**
     * Creates a new entity in the persons table from the supplied person-thing.
     * Returns true if successful
     *
     * @param thing
     */
    @Override
    public boolean create(Person thing)
    {
        System.out.println(thing);

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO `motorhome`.`person` (`password`, `firstName`, `lastName`, `email`," +
                    " `siteRole`, `birthDate`, `joinDate`, `Address_idAddress`) " +
                    "VALUES (?,?,?,?,?,?,?,?);");

            preparedStatement.setString(1, thing.getPassword());
            preparedStatement.setString(2, thing.getFirstName());
            preparedStatement.setString(3, thing.getLastName());
            preparedStatement.setString(4, thing.getEmail());
            preparedStatement.setString(5, thing.getUserType().toString());
            preparedStatement.setDate(6, Date.valueOf(thing.getBirthDate()));
            preparedStatement.setDate(7, Date.valueOf(thing.getJoinDate()));
            preparedStatement.setInt(8, thing.getAddress().getAddressID());

            int rowsWritten = preparedStatement.executeUpdate();// e.Update returns n of row written
            System.out.println("PersonDAO::create >> executeUpdate returned " + rowsWritten);

            return rowsWritten > 0; // above 0 is counted as success.

        } catch(SQLException e)
        {/* we let exceptions flow along, but probably should log*/
            e.printStackTrace();
        }

        /* If we reach here, stuff went wrong, return false */
        return false;
    }

    /**
     * Returns a Person object, read from database - queried with supplied id.
     * Returns null if there is no person by id in db.
     *
     * @param id
     */
    @Override
    public Person read(Integer id)
    {
        Person person = new Person();
        /*PreparedStatement might throw, so we gotta catch */
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.person WHERE idPerson = ?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery(); // e.Query return a resultset
            /* it is technically possible for there to be multiple returns, iwc last Person is actually
            returned: shouldn't happen because of id-auto-increment on DB-side */
            while(resultSet.next())
            {
                fillInPersonValues(person, resultSet);
                /* for testing */
                System.out.println("PersonDAO::read >> query returned meaningful person, id:" + person.getPersonID() + " and name " + person.getFullName());
            }

        } catch(SQLException e) { e.printStackTrace(); }
        /* we decided to go the null-route, so null-check returned Person-object*/
        return person;
    }

    /**
     * Helper method fills in values from resultset på Person object. There is nothing to return as the
     * person is supplied
     */
    private void fillInPersonValues(Person person, ResultSet resultSet)
    {
        try
        {
            person.setPersonID(resultSet.getInt(1));
            person.setPassword(resultSet.getString(2));
            person.setFirstName(resultSet.getString(3));
            person.setLastName(resultSet.getString(4));
            person.setEmail(resultSet.getString(5));
            person.setUserType(SiteRole.valueOf(resultSet.getString(6)));
            person.setBirthDate(resultSet.getDate(7).toLocalDate());
            person.setJoinDate(resultSet.getDate(8).toLocalDate());
            person.setAddress(SiteDAOCollection.getInstance().addressDAO().read(resultSet.getInt(9)));
            // done, nothing to return
        } catch(SQLException e) { e.printStackTrace(); }
    }

    /** Method queries DB and returns complete list of those objects
    * as their java-object parallels wrapped in a List*/
    @Override
    public List<Person> readall()
    {/*List to contain all Person objects from DB*/
        List<Person> persons = new ArrayList<>();

        try
        {/* PreparedStatement to create query for db */
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM motorhome.person");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Person person = new Person();
                fillInPersonValues(person, resultSet); // values attributed
                persons.add(person); // assembled person added to returned list
            }
            /* letting exceptions flow for this iteration */
        } catch(SQLException e) { e.printStackTrace(); }
        return persons;
    }

    /**
     * Method executes update to DB based on supplied Person-thing:
     * Returns true if update was written to DB.
     * @param thing
     */
    @Override
    public boolean update(Person thing)
    {
        System.out.println("\n\n\n Show me the data!\n"
                          +thing.toString());

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE motorhome.person SET " +
                    "password = ?," +
                    "firstName = ?," +
                    "lastName = ?," +
                    "email = ?," +
                    "siteRole = ?," +
                    "birthDate = ?," +
                    "joinDate = ?" +
                    "WHERE idPerson = ?");
            /*filling in the attribute values from Person object, id last because of 'preppedSt.' */
            preparedStatement.setString(1, thing.getPassword());
            preparedStatement.setString(2, thing.getFirstName());
            preparedStatement.setString(3, thing.getLastName());
            preparedStatement.setString(4, thing.getEmail());
            preparedStatement.setString(5, thing.getUserType().toString());
            preparedStatement.setDate(6, Date.valueOf(thing.getBirthDate()));
            preparedStatement.setDate(7, Date.valueOf(thing.getJoinDate()));
            preparedStatement.setInt(8, thing.getPersonID());

            /* e.Update returns the num of rows manipulated. */
            int numChanges = preparedStatement.executeUpdate();

            /* Address is updated here, adhering the principle of least astonishment rather than
            * insisting on low coupling */
            SiteDAOCollection.getInstance().addressDAO().update(thing.getAddress());

            /* if numChanges above zero, DB was written to */
            return numChanges > 0;
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        /* reaching here means things went awry */
        return false;
    }

    /**
     * Method removes lines from motorhome.person table, where idPerson = id supplied
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.person WHERE idPerson = ?");

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

            /* still doing nothing, should really log */
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }
}