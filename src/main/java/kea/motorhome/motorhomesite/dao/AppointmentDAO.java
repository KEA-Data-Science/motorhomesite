package kea.motorhome.motorhomesite.dao;
// by kcn
import kea.motorhome.motorhomesite.models.Appointment;
import kea.motorhome.motorhomesite.util.DBConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class facilitates interaction between datalayer and business-logic layer of application in
 * regards to Appointment objects.
 * Near-redundancies with ReservationDAO have been embranced because of a possible greater
 * divide between functionality in future iterations.
 */
public class AppointmentDAO implements IDAO<Appointment, Integer>
{

    private Connection connection;

    public AppointmentDAO(){ connection = DBConnectionManager.getConnection();}

    /**
     * Creates a new entity in data-source based on the supplied thing.
     * Returns true if successful.
     *
     * @param thing
     */
    @Override
    public boolean create(Appointment thing)
    {
        try
        {   /* preparing statement to create appointment in db / insert row in appointment table */
            PreparedStatement preparedStatement = getPreparedStatementToCreateAppointment(thing);

            if(preparedStatement.executeUpdate() > 0) /* try to insert row, if insert worked */
            {
                /* updating address here to satisfy principle of least astonishment */
//                dao().addressDAO().update(thing.getAddress());

                ResultSet generatedKey = preparedStatement.getGeneratedKeys(); /* fetch key / new id of app */

                if(generatedKey.next()) /* if a key is there */
                { /* write to employee_has_appointment junction table, for each id attached in list */
                    deleteRowsFromReservation_has_AppointmentTable(generatedKey.getInt(1));

                    for(Integer employeeID : thing.getEmployeeIDs())
                    {

                        writeRowToEmployee_has_AppointmentTable(generatedKey.getInt(1), employeeID);
                    }

                    return preparedStatement.executeUpdate() > 0;
                }
            }

        } catch(SQLException e) {e.printStackTrace();}

        return false;
    }

    /**
     * Method sets parameters of prepared statement, with appointment attributes
     * mentioned in the sequence the occur in the Appointment class (ignoring appointmentID)
     * NOTE: prepared statment is set to containt generated key
     */
    private PreparedStatement getPreparedStatementToCreateAppointment(Appointment thing) throws
                                                                                         SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO motorhome.appointment " +
                "(date, time, notes, distance, Address_idAddress, Motorhome_idMotorhome)" +
                " VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);


        preparedStatement.setDate(1, Date.valueOf(thing.getDate()));
        preparedStatement.setTime(2, Time.valueOf(thing.getTime()));
        preparedStatement.setString(3, thing.getNotes());
        preparedStatement.setFloat(4, thing.getDistance());
        preparedStatement.setInt(5, thing.getAddress().getAddressID());
        preparedStatement.setInt(6, thing.getMotorHomeID());

        return preparedStatement;
    }

    /**
     * Method writes a row to the Employee_has_Appointment junction table
     */
    private void writeRowToEmployee_has_AppointmentTable(int appointmentID, int employeeID)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.employee_has_appointment" +
                    "(Employee_idEmployee, Appointment_idAppointment) VALUES (?,?)");
            preparedStatement.setInt(1, employeeID);
            preparedStatement.setInt(2, appointmentID);

            preparedStatement.executeUpdate();
        } catch(SQLException e) {e.printStackTrace();}
    }

    /**
     * Returns a T-type object, read from data-source
     * - queried with supplied id (type U).
     * Returns null if there is no 'thing' by id in db.
     *
     * @param id
     */
    @Override
    public Appointment read(Integer id)
    {
        Appointment appointment = new Appointment();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.appointment WHERE idAppointment = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){ appointment = reconstructAppointmentFromResultSet(resultSet); }

            return appointment;

        } catch(SQLException e) {e.printStackTrace();}


        return null;
    }

    private Appointment reconstructAppointmentFromResultSet(ResultSet resultSet) throws SQLException
    {
        Appointment appointment = new Appointment();

        appointment.setAppointmentID(resultSet.getInt(1));
        appointment.setDate(resultSet.getDate(2).toLocalDate());
        appointment.setTime(resultSet.getTime(3).toLocalTime());
        appointment.setNotes(resultSet.getString(4));
        appointment.setDistance(resultSet.getFloat(5));
        appointment.setAddress(dao().addressDAO().read(resultSet.getInt(6)));
        appointment.setMotorHomeID(resultSet.getInt(7));

        appointment.setEmployeeIDs(reconstructEmployeeIDListForAppointment(appointment.getAppointmentID()));

        return appointment;
    }

    public SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    /**
     * Method queries the junction table, looking for the right appointment-id/employee-id match
     * and reconstructing an Integer-List for supplied appointment.
     * ** Method is a duplicate of the one in ReservationDAO; duplication rather than bad entanglement;
     * Maybe we should implement smth like 'JunctionDAO'; here all junction-table activities would reside.
     */
    private List<Integer> reconstructEmployeeIDListForAppointment(int appointmentID) throws SQLException
    {
        List<Integer> employeeIDs = new ArrayList<>();

        /* employee IDs associated with the reservation must be recovered for list of Integers */
        PreparedStatement associatedEmployeesStatement = connection.prepareStatement(
                "SELECT e.idEmployee " +
                "FROM motorhome.employee_has_appointment eha " +
                "JOIN motorhome.employee e " +
                "ON eha.Employee_idEmployee = e.idEmployee " +
                "WHERE eha.Appointment_idAppointment = ?;");

        associatedEmployeesStatement.setInt(1, appointmentID);

        ResultSet employeesResultSet = associatedEmployeesStatement.executeQuery();
        /* going through result-set, extracting an Employee each iteration*/
        while(employeesResultSet.next())
        {
            employeeIDs.add(employeesResultSet.getInt(1));
        }

        return employeeIDs;
    }

    /**
     * Method queries data-source and returns complete list of paralleled
     * object of type T wrapped in a List
     */
    @Override
    public List<Appointment> readall()
    {
        List<Appointment> appointments = new ArrayList<>();

        try
        {
            PreparedStatement allAppointmentsStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.appointment");

            ResultSet resultSet = allAppointmentsStatement.executeQuery();

            while(resultSet.next())
            {
                Appointment appointment = reconstructAppointmentFromResultSet(resultSet);
                appointments.add(appointment);
            }

        } catch(SQLException e) {e.printStackTrace();}

        return appointments;
    }

    /**
     * Method executes update to DB based on supplied thing type-T:
     * Returns true if update was written to DB, false if nothing was written.
     *
     * @param thing
     */
    @Override
    public boolean update(Appointment thing)
    {
        try
        {
            /* PreparedStatment to UPDATE values of appointment-thing */
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE `motorhome`.`appointment` SET" +
                    " `date` = ?," +
                    " `time` = ?," +
                    " `notes` = ?," +
                    " `distance` = ?," +
//                    " `Address_idAddress` = ?," +
                    " `Motorhome_idMotorhome` = ?" +
                    " WHERE (`idAppointment` = ?);");

            preparedStatement.setDate(1, Date.valueOf(thing.getDate()));
            preparedStatement.setTime(2, Time.valueOf(thing.getTime()));
            preparedStatement.setString(3, thing.getNotes());
            preparedStatement.setFloat(4, thing.getDistance());
//            preparedStatement.setInt(5, thing.getAddress().getAddressID());
            preparedStatement.setInt(5, thing.getMotorHomeID());
            preparedStatement.setInt(6, thing.getAppointmentID());

            /* Execute update -> and if at least one row was affected */
            if(preparedStatement.executeUpdate() > 0)
            {

                /* Clear and refill employee_has_appointment table rows related to appointment */
                deleteRowsFromEmployee_has_AppointmentTable(thing.getAppointmentID());

                for(Integer employeeID : thing.getEmployeeIDs())
                {
                    writeRowToEmployee_has_AppointmentTable(thing.getAppointmentID(), employeeID);
                }

                /* if no errors were thrown, we assume transactions went well. Eyes-on-validation... */
                return true;
            }
        } catch(SQLException e) {e.printStackTrace();}
        return false;
    }

    /**
     * Method deletes a row from Employee_has_Appointment-junction table that match supplied appointment ID
     */
    private void deleteRowsFromEmployee_has_AppointmentTable(int appointmentID) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM motorhome.employee_has_appointment WHERE Appointment_idAppointment = ?");
        preparedStatement.setInt(1, appointmentID);
        preparedStatement.executeUpdate();
    }

    /**
     * Method deletes a row from Reservation_has_Appointment-junction table that match supplied
     * appointment ID.
     */
    private void deleteRowsFromReservation_has_AppointmentTable(int appointmentID) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM motorhome.reservation_has_appointment WHERE Appointment_idAppointment = ?");
        preparedStatement.setInt(1,appointmentID);
        preparedStatement.executeUpdate();
    }


    /**
     * Method removes object from data-source, where data-source entity-id
     * equals the supplied id, and returns a thing of type U
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        try
        {
            deleteRowsFromReservation_has_AppointmentTable(id);
            deleteRowsFromEmployee_has_AppointmentTable(id);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM `motorhome`.`appointment` WHERE (`idAppointment` = ?);");
            preparedStatement.setInt(1, id);

            if(preparedStatement.executeUpdate() > 0)
            {


                return true;
            }

        } catch(SQLException e) {e.printStackTrace();}
        return false;
    }
}