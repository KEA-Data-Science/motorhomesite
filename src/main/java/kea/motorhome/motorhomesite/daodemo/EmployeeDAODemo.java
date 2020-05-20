package kea.motorhome.motorhomesite.daodemo;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAODemo implements IDAO<Employee, Integer> {

    List<Employee> employees;

    public EmployeeDAODemo()
    {
        employees = new ArrayList<>();

        Address address1 = new Address(1, "Danmark", "Kastrupvej","15B","1900");
        Person person1 = new Person(3, "Anna", "Adminsen", address1, LocalDate.now().minusYears(2),
                SiteRole.ADMIN,
                LocalDate.now());

        employees.add(new Employee(1, person1,1111));

        Address address2 = new Address(2, "Danmark", "Brønshøjvej","Nr 1.","2600");
        Person person2 = new Person(4, "Søren", "Sælgersen", address2, LocalDate.now().minusYears(4),
                SiteRole.SALES,
                LocalDate.now());
        employees.add(new Employee(2, person2,2222));
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
