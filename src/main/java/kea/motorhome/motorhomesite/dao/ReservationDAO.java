package kea.motorhome.motorhomesite.dao;
// kcn

import kea.motorhome.motorhomesite.enums.ReservationStatus;
import kea.motorhome.motorhomesite.models.*;
import kea.motorhome.motorhomesite.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationDAO implements IDAO<Reservation, Integer>
{
    Connection connection;

    public ReservationDAO()
    {
        connection = DBConnectionManager.getConnection();
    }

    /**
     * Creates a new entity in data-source based on the supplied thing.
     * Returns true if successful.
     *
     * @param thing
     */
    @Override
    public boolean create(Reservation thing)
    {

        if(Objects.isNull(thing)){return false;} // thing might be null
        try
        {
            PreparedStatement reservationStatement;
            reservationStatement = preparedStatementToWriteRowToReservationTable(thing);
            if(reservationStatement.executeUpdate() > 0)
            {
                ResultSet generatedKeys = reservationStatement.getGeneratedKeys();

                if(generatedKeys.next())
                {
                    int newID = generatedKeys.getInt(1);

                    /* placing rows in the Reservation_has_Service junction table */
                    writeRowsToReservation_has_ServiceTable(thing.getServices(), newID);

                    /*for eaching list of appointments on reservation; writing rows in  */
                    writeRowsToReservation_has_AppointmentTable(thing.getAppointments(), newID);
                }
            }
        } catch(SQLException e) {e.printStackTrace();}

        return false;
    }

    private PreparedStatement preparedStatementToWriteRowToReservationTable(Reservation thing) throws
                                                                                               SQLException
    {
        PreparedStatement reservationStatement;
        reservationStatement = connection.prepareStatement(
                "INSERT INTO motorhome.reservation (notes, internalNotes, reservationStatus," +
                " Employee_idEmployee, Period_idPeriod, Motorhome_idMotorhome, " +
                "Customer_driversLicense) VALUES (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);

        /* setting atomic fields from java, mapping to sql */
        reservationStatement.setString(1, thing.getNotes());
        reservationStatement.setString(2, thing.getInternalNotes());
        reservationStatement.setString(3, thing.getStatus().name());
        reservationStatement.setInt(4, thing.getEmployee().getEmployeeID());
        /*  period is written to data layer at controller layer */
        reservationStatement.setInt(5, thing.getPeriod().getPeriodID());
        reservationStatement.setInt(6, thing.getMotorhome().getMotorhomeID());
        reservationStatement.setString(7, thing.getCustomer().getDriversLicence());


        return reservationStatement;
    }

    private void writeRowsToReservation_has_ServiceTable(List<Service> services, int reservationID) throws
                                                                                                    SQLException
    {
        for(Service service : services)
        {
            PreparedStatement reservationsServicesStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.reservation_has_service " +
                    "(Service_idService, Reservation_idReservation) VALUES (?,?)");
            reservationsServicesStatement.setInt(1, service.getServiceID());
            reservationsServicesStatement.setInt(2, reservationID);
            reservationsServicesStatement.executeUpdate();
        }
    }

    private void writeRowsToReservation_has_AppointmentTable(List<Appointment> appointments, int reservationsID) throws
                                                                                                                  SQLException
    {
        for(Appointment appointment : appointments)
        {
            PreparedStatement reservationAppointmentsStatement = connection.prepareStatement(
                    "INSERT INTO motorhome.reservation_has_appointment" +
                    "(Appointment_idAppointment, Reservation_idReservation) VALUES (?,?)");
            reservationAppointmentsStatement.setInt(1, appointment.getAppointmentID());
            reservationAppointmentsStatement.setInt(2, reservationsID);
            reservationAppointmentsStatement.executeUpdate();
        }
    }

    /**
     * Returns a T-type object, read from data-source
     * - queried with supplied id (type U).
     * Returns null if there is no 'thing' by id in db.
     *
     * @param id
     */
    @Override
    public Reservation read(Integer id)
    {
        Reservation reservation = new Reservation();

        try
        {
            /* cook up statement*/
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.reservation WHERE idReservation = ?");
            /* enter details; set id */
            preparedStatement.setInt(1, id);
            /* execute statement */
            ResultSet reservationResultSet = preparedStatement.executeQuery();
            /* roll up the main part of reservation */
            if(reservationResultSet.next())
            {
                return reconstructReservationFromResultSet(reservationResultSet);  // return initialized object
            }

        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    private Reservation reconstructReservationFromResultSet(ResultSet reservationResultSet) throws
                                                                                            SQLException
    {
        Reservation reservation = new Reservation();

        try
        {
            reservation.setReservationID(reservationResultSet.getInt(1));
            reservation.setNotes(reservationResultSet.getString(2));
            reservation.setInternalNotes(reservationResultSet.getString(3));
            reservation.setStatus(ReservationStatus.status(reservationResultSet.getString(4)));
            reservation.setEmployee(SiteDAOCollection.getInstance().employeeDAO().read(reservationResultSet.getInt(5)));
            reservation.setPeriod(SiteDAOCollection.getInstance().periodDAO().read(reservationResultSet.getInt(6)));
            reservation.setMotorhome(SiteDAOCollection.getInstance().motorhomeDAO().read(reservationResultSet.getInt(7)));
            reservation.setCustomer(SiteDAOCollection.getInstance().customerDAO().read(reservationResultSet.getString(8)));

            /* recreate list of appointments from rs */
            reservation.setAppointments(reconstructAppointmentsListForReservation(reservation.getReservationID()));

            /* recreate list of services for reservation */
          reservation.setServices(reconstructServicesListForReservation(reservation.getReservationID()));

            return reservation;
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method queries the junction table, looking for the right appointment-id/reservation-id match
     * and reconstructing an Appointments-List for supplied reservation.
     */
    private List<Appointment> reconstructAppointmentsListForReservation(int reservationID) throws SQLException
    {
        List<Appointment> appointments = new ArrayList<>();

        /* assemble appointments list from junction table */
        PreparedStatement appointmentsByIDStatement = connection.prepareStatement(
                "SELECT app.idAppointment," +
                "app.date," +
                "app.time," +
                "app.notes," +
                "app.distance," +
                "app.Address_idAddress," +
                "app.Motorhome_idMotorhome" +
                "           FROM motorhome.reservation_has_appointment link" +
                "                   JOIN motorhome.appointment app" +
                "                    ON link.Appointment_idAppointment = app.idAppointment" +
                "                    WHERE link.Reservation_idReservation = ?;");

        appointmentsByIDStatement.setInt(1, reservationID);

        ResultSet appointmentsResults = appointmentsByIDStatement.executeQuery();

        while(appointmentsResults.next())
        {
            Appointment appointment = new Appointment();
            appointment.setAppointmentID(appointmentsResults.getInt(1));
            appointment.setDate(appointmentsResults.getDate(2).toLocalDate());
            appointment.setTime(appointmentsResults.getTime(3).toLocalTime());
            appointment.setNotes(appointmentsResults.getString(4));
            appointment.setDistance(appointmentsResults.getFloat(5));
            appointment.setAddress(SiteDAOCollection.getInstance().addressDAO().read(appointmentsResults.getInt(6)));
            appointment.setMotorHomeID(appointmentsResults.getInt(7));
            /* assembling list of associated */
            appointment.setEmployeeIDs(reconstructEmployeeIDListForAppointment(appointment.getAppointmentID()));

            appointments.add(appointment);
        }

        return appointments;
    }

    /**
     * Method queries the junction table, looking for the right appointment-id/employee-id match
     * and reconstructing an Integer-List for supplied appointment.
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

    private List<Service> reconstructServicesListForReservation(int reservationID)
    {
        List<Service> services = new ArrayList<>();

        try
        {
            PreparedStatement serviceAssWithReservationID = connection.prepareStatement(
                    "SELECT s.idService, " +
                    "s.name, " +
                    "s.description," +
                    "s.unitPrice " +
                    "FROM motorhome.reservation_has_service rhs " +
                    "JOIN motorhome.service s " +
                    "ON rhs.Service_idService = s.idService " +
                    "WHERE rhs.Reservation_idReservation =?;");
            serviceAssWithReservationID.setInt(1, reservationID);

            ResultSet serviceRowsResultSet = serviceAssWithReservationID.executeQuery();

            while(serviceRowsResultSet.next())
            {
                Service service = new Service();
                service.setServiceID(serviceRowsResultSet.getInt(1));
                service.setName(serviceRowsResultSet.getString(2));
                service.setDescription(serviceRowsResultSet.getString(3));
                service.setUnitPrice(serviceRowsResultSet.getFloat(4));
                services.add(service);
            }
        } catch(SQLException e) {e.printStackTrace();}

        return services;
    }

    /**
     * Method queries data-source and returns complete list of paralleled
     * object of type T wrapped in a List
     */
    @Override
    public List<Reservation> readall()
    {
        List<Reservation> allReservations = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM motorhome.reservation;");

            ResultSet reservationsResultSet = preparedStatement.executeQuery();

            while(reservationsResultSet.next())
            {
                Reservation reservation = reconstructReservationFromResultSet(reservationsResultSet);

                if(reservation != null){allReservations.add(reservation);}
            }

            preparedStatement.close(); // closing connection on statement

        } catch(SQLException e) {e.printStackTrace();}

        return allReservations;
    }

    /**
     * Method executes update to DB based on supplied thing type-T:
     * Returns true if update was written to DB, false if nothing was written.
     *
     * @param thing
     */
    @Override
    public boolean update(Reservation thing)
    {
        try
        {
            /* setting simple fields through 1st PreppedS */
            PreparedStatement updateReservationStatement = connection.prepareStatement(
                    "UPDATE motorhome.reservation SET " +
                    "notes = ?," +
                    "internalNotes = ?, " +
                    "reservationStatus = ?," +
                    "Employee_idEmployee = ?," +
                    "Motorhome_idMotorhome = ?," +
                    "Customer_driversLicense = ?" +
                    "WHERE idReservation = ?");
            updateReservationStatement.setString(1, thing.getNotes());
            updateReservationStatement.setString(2, thing.getInternalNotes());
            updateReservationStatement.setString(3, thing.getStatus().toString());
            updateReservationStatement.setInt(4, thing.getEmployee().getEmployeeID());
            updateReservationStatement.setInt(5, thing.getMotorhome().getMotorhomeID());
            updateReservationStatement.setString(6, thing.getCustomer().getDriversLicence());
            updateReservationStatement.setInt(7, thing.getReservationID());

            updateReservationStatement.executeUpdate();

            /* reservation period is update | this is a little dirty; a possibly opaque coupling */
            SiteDAOCollection.getInstance().periodDAO().update(thing.getPeriod());

            /* delete all rows associated with reservation in reservation_has_service-tale */
            removeEntriesFromReservationServiceJunction(thing.getReservationID());
            /* then add those associated service/reservations to the same junction table */
            writeRowsToReservation_has_ServiceTable(thing.getServices(), thing.getReservationID());


            /* find appointments no longer associated with thing; do same as immediately above */
            removeEntriesFromReservationAppointmentJunction(thing.getReservationID());
            writeRowsToReservation_has_AppointmentTable(thing.getAppointments(), thing.getReservationID());

            return true;
        } catch(SQLException e) {e.printStackTrace();}

        return false;
    }

    private void removeEntriesFromReservationServiceJunction(int reservationID) throws SQLException
    {
        PreparedStatement deleteAssServicesStatement = connection.prepareStatement(
                "DELETE FROM motorhome.reservation_has_service" +
                " WHERE Reservation_idReservation = ?");
        deleteAssServicesStatement.setInt(1, reservationID);
        deleteAssServicesStatement.executeUpdate();
    }

    private void removeEntriesFromReservationAppointmentJunction(int reservationID) throws SQLException
    {
        PreparedStatement deleteAssAppointmentsStatement = connection.prepareStatement(
                "DELETE FROM motorhome.reservation_has_appointment " +
                "WHERE Reservation_idReservation = ?");
        deleteAssAppointmentsStatement.setInt(1, reservationID);
        deleteAssAppointmentsStatement.executeUpdate();
    }

    /**
     * Method removes object from data-source, where data-source entity-id
     * equals the supplied id, and returns a thing of type U.
     * Method also updates/delete information from associated junction tables
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id)
    {
        try
        {
            PreparedStatement deleteReservationStatement = connection.prepareStatement(
                    "DELETE FROM motorhome.reservation WHERE idReservation = ?");
            deleteReservationStatement.setInt(1, id);

            /* delete all rows associated with reservation in reservation_has_service-tale */
            removeEntriesFromReservationServiceJunction(id);
            removeEntriesFromReservationAppointmentJunction(id);

            // this update needs to happen after deletion of junction-table rows
            deleteReservationStatement.executeUpdate();
            return true;

        } catch(SQLException e) { e.printStackTrace();}

        return false;
    }
}