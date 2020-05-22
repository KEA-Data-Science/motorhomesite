package kea.motorhome.motorhomesite.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Appointment
{
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime time;
    private Address address;
    private int motorHomeID;
    private List<Integer> employeeIDs;
    private String notes;
    private int getAppointmentID;
    private float distance; // distance to appointment from start (company)

    public Appointment(){}

    public Appointment(LocalDate date, LocalTime time, Address address, int motorHomeID, List<Integer> employeeIDs, String notes, int getAppointmentID, float distance)
    {
        this.date = date;
        this.time = time;
        this.address = address;
        this.motorHomeID = motorHomeID;
        this.employeeIDs = employeeIDs;
        this.notes = notes;
        this.getAppointmentID = getAppointmentID;
        this.distance = distance;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public int getMotorHomeID()
    {
        return motorHomeID;
    }

    public void setMotorHomeID(int motorHomeID)
    {
        this.motorHomeID = motorHomeID;
    }

    public List<Integer> getEmployeeIDs()
    {
        return employeeIDs;
    }

    public void setEmployeeIDs(List<Integer> employeeIDs)
    {
        this.employeeIDs = employeeIDs;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public int getGetAppointmentID()
    {
        return getAppointmentID;
    }

    public void setGetAppointmentID(int getAppointmentID)
    {
        this.getAppointmentID = getAppointmentID;
    }

    public float getDistance()
    {
        return distance;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof Appointment)) return false;
        Appointment that = (Appointment)o;
        return getMotorHomeID() == that.getMotorHomeID() &&
               getGetAppointmentID() == that.getGetAppointmentID() &&
               Float.compare(that.getDistance(), getDistance()) == 0 &&
               Objects.equals(getDate(), that.getDate()) &&
               Objects.equals(getTime(), that.getTime()) &&
               Objects.equals(getAddress(), that.getAddress()) &&
               Objects.equals(getEmployeeIDs(), that.getEmployeeIDs()) &&
               Objects.equals(getNotes(), that.getNotes());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getDate(), getTime(), getAddress(), getMotorHomeID(), getEmployeeIDs(), getNotes(), getGetAppointmentID(), getDistance());
    }

    @Override
    public String toString()
    {
        return "Appointment{" +
               "date=" + date +
               ", time=" + time +
               ", address=" + address +
               ", motorHomeID=" + motorHomeID +
               ", employeeIDs=" + employeeIDs +
               ", notes='" + notes + '\'' +
               ", getAppointmentID=" + getAppointmentID +
               ", distance=" + distance +
               '}';
    }
}