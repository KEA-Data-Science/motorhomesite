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

    public Appointment(){}

    public Appointment(LocalDate date, LocalTime time, Address address, int motorHomeID, List<Integer> employeeIDs, String notes, int getAppointmentID)
    {
        this.date = date;
        this.time = time;
        this.address = address;
        this.motorHomeID = motorHomeID;
        this.employeeIDs = employeeIDs;
        this.notes = notes;
        this.getAppointmentID = getAppointmentID;
    }

    public LocalDate getDate(){ return date; }

    public void setDate(LocalDate date){ this.date = date; }

    public LocalTime getTime(){ return time; }

    public void setTime(LocalTime time){ this.time = time; }

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

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment)o;
        return motorHomeID == that.motorHomeID &&
               Objects.equals(date, that.date) &&
               Objects.equals(address, that.address) &&
               Objects.equals(employeeIDs, that.employeeIDs) &&
               Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(date, address, motorHomeID, employeeIDs, notes);
    }

    @Override
    public String toString()
    {
        return "Appointment{" +
               "date=" + date +
               ", address=" + address +
               ", motorHomeID=" + motorHomeID +
               ", employeeIDs=" + employeeIDs +
               ", notes='" + notes + '\'' +
               '}';
    }
}