package kea.motorhome.motorhomesite.daodemo;
// by LNS
import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Appointment;

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
            if (appointment.getAppointmentID() == id)
                return appointment;
        return null;
    }

    @Override
    public boolean update(Appointment appointment)
    {
        for (int i = 0; i < appointments.size(); i++)
        {
            if (appointments.get(i).getAppointmentID() == appointment.getAppointmentID()) {
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
            if (id == appointment.getAppointmentID())
                return appointments.remove(appointment);
        }
        return false;
    }
}
