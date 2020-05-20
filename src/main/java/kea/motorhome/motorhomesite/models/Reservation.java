package kea.motorhome.motorhomesite.models;


import kea.motorhome.motorhomesite.enums.ReservationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class model the reservation of a single vehicle by a single customer.
 * Notes: the getters and setters of this class follow the builder-style experimentally;
 * All setters return 'this' class instance
 */
public class Reservation
{
    private ReservationStatus status;

    private int reservationID;
    private Customer customer;
    private Employee employee;
    private Period period;
    private Motorhome motorhome;
    private List<Service> services;
    private String notes;
    private String internalNotes;

    private List<Appointment> appointments;

    public Reservation()
    {
    }

    public Reservation(ReservationStatus status, int reservationID, Customer customer, Employee employee, Period period, Motorhome motorhome, List<Service> services, String notes, String internalNotes, List<Appointment> appointments)
    {
        this.status = status;
        this.reservationID = reservationID;
        this.customer = customer;
        this.employee = employee;
        this.period = period;
        this.motorhome = motorhome;
        this.services = services;
        this.notes = notes;
        this.internalNotes = internalNotes;
        this.appointments = appointments;
    }

    public ReservationStatus getStatus()
    {
        return status;
    }

    public Reservation setStatus(ReservationStatus status)
    {
        this.status = status;
        return this;
    }

    public int getReservationID()
    {
        return reservationID;
    }

    public Reservation setReservationID(int reservationID)
    {
        this.reservationID = reservationID;
        return this;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public Reservation setCustomer(Customer customer)
    {
        this.customer = customer;
        return this;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public Reservation setEmployee(Employee employee)
    {
        this.employee = employee;
        return this;
    }

    public Period getPeriod()
    {
        return period;
    }

    public Reservation setPeriod(Period period)
    {
        this.period = period;
        return this;
    }

    public Motorhome getMotorhome()
    {
        return motorhome;
    }

    public Reservation setMotorhome(Motorhome motorhome)
    {
        this.motorhome = motorhome;
        return this;
    }

    public List<Service> getServices()
    {
        return services;
    }

    public Reservation setServices(List<Service> services)
    {
        this.services = services;
        return this;
    }

    public String getNotes()
    {
        return notes;
    }

    public Reservation setNotes(String notes)
    {
        this.notes = notes;
        return this;
    }

    public String getInternalNotes()
    {
        return internalNotes;
    }

    public Reservation setInternalNotes(String internalNotes)
    {
        this.internalNotes = internalNotes;
        return this;
    }

    public List<Appointment> getAppointments()
    {
        return appointments;
    }

    public Reservation setAppointments(List<Appointment> appointments)
    {
        this.appointments = appointments;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof Reservation)) return false;
        Reservation that = (Reservation)o;
        return getReservationID() == that.getReservationID() &&
               getStatus() == that.getStatus() &&
               Objects.equals(getCustomer(), that.getCustomer()) &&
               Objects.equals(getEmployee(), that.getEmployee()) &&
               Objects.equals(getPeriod(), that.getPeriod()) &&
               Objects.equals(getMotorhome(), that.getMotorhome()) &&
               Objects.equals(getServices(), that.getServices()) &&
               Objects.equals(getNotes(), that.getNotes()) &&
               Objects.equals(getInternalNotes(), that.getInternalNotes()) &&
               Objects.equals(getAppointments(), that.getAppointments());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getStatus(), getReservationID(), getCustomer(), getEmployee(), getPeriod(), getMotorhome(), getServices(), getNotes(), getInternalNotes(), getAppointments());
    }

    @Override
    public String toString()
    {
        return "Reservation{" +
               "status=" + status +
               ", reservationID=" + reservationID +
               ", customer=" + customer +
               ", employee=" + employee +
               ", period=" + period +
               ", motorhome=" + motorhome +
               ", services=" + services +
               ", notes='" + notes + '\'' +
               ", internalNotes='" + internalNotes + '\'' +
               ", appointments=" + appointments +
               '}';
    }

}