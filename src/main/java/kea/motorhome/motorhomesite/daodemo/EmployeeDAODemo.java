package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.models.Appointment;
import kea.motorhome.motorhomesite.models.Employee;
import kea.motorhome.motorhomesite.models.Motorhome;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAODemo implements IDAO<Employee, Integer> {

    List<Employee> employees;

    public EmployeeDAODemo()
    {
        employees = new ArrayList<>();
    }
    @Override
    public boolean create(Employee thing) {
        return employees.add(thing);
    }

    @Override
    public List<Employee> readall()
    {
        return employees;
    }

    public Employee read(Integer id)
    {
        for (Employee employee : employees)
            if (employee.getEmployeeID() == id)
                return employee;
        return null;
    }

    @Override
    public boolean update(Employee employee)
    {
        for (int i = 0; i < employees.size(); i++)
        {
            if (employees.get(i).getEmployeeID() == employee.getEmployeeID()) {
                employees.set(i, employee);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        for (Employee employee : employees)
        {
            if (id == employee.getEmployeeID())
                return employees.remove(employee);
        }
        return false;
    }
}
