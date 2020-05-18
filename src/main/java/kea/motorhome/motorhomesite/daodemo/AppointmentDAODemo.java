package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Appointment;
import kea.motorhome.motorhomesite.models.Employee;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDAODemo implements IDAO<Appointment, Integer> {
    List<Appointment> appointments;

    public AppointmentDAODemo()
    {
        appointments = new ArrayList<>();
    }
    @Override
    public boolean create(Appointment thing) {
        return appointments.add(thing);
    }

    @Override
    public List<Appointment> readall()
    {
        return appointments;
    }

    public Appointment read(Integer id)
    {
        for (Appointment appointment : appointments)
            if (appointment.getGetAppointmentID() == id)
                return appointment;
        return null;
    }

    @Override
    public boolean update(Appointment appointment)
    {
        for (int i = 0; i < appointments.size(); i++)
        {
            if (appointments.get(i).getGetAppointmentID() == appointment.getGetAppointmentID()) {
                appointments.set(i, appointment);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (Appointment appointment : appointments)
        {
            if (id == appointment.getGetAppointmentID())
                return appointments.remove(appointment);
        }
        return false;
    }
}
