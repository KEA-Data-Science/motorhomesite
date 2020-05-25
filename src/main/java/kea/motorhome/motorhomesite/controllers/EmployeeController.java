package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.EmployeeDAODemo;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController
{

    private SiteDAOCollection dao;

    public EmployeeController() { dao = SiteDAOCollection.getInstance(); }

    @GetMapping("/employees/employees")
    public String employeesPage(Model model){
        model.addAttribute("employees" , dao.employeeDAO().readall());
        return "employees/employees";
    }

    @GetMapping("/employees/details")
    public String getEmployeeByParameter(Model model, @RequestParam int id) {
        model.addAttribute("employee", dao.employeeDAO().read(id));
        return "employees/details";
    }

    @GetMapping("/employees/new")
    public String showNewEmployeeForm(Model model) {
        Employee employee = new Employee();
        employee.setEmployeeID(dao.employeeDAO().readall().size()+1);
        employee.setAccountancyID(dao.employeeDAO().readall().size()+1);
        Person person = new Person();
        person.setPersonID(dao.personDAO().readall().size()+1);
        employee.setPerson(person);
        Address address = new Address();
        address.setAddressID(dao.addressDAO().readall().size()+1);
        employee.getPerson().setAddress(address);
        model.addAttribute("employee", employee);
        model.addAttribute("person", person);
        model.addAttribute("address", address);
        return "employees/new";
    }

    @RequestMapping(value ="/saveemployee", method = RequestMethod.POST)
    public String createEmployee(@ModelAttribute("employee") Employee employee,
                                 @ModelAttribute("person") Person person,
                                 @ModelAttribute("address") Address address) {
        dao.addressDAO().create(address);
        dao.personDAO().create(person);
        dao.employeeDAO().create(employee);
        return "redirect:/employees/employees";
    }

    @RequestMapping(value ="/updateemployee", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute("employee") Employee employee) {
        dao.employeeDAO().update(employee);
        return "redirect:/employees/employees";
    }

    @GetMapping("/employees/edit")
    public String showEditEmployeeForm(Model model, @RequestParam int id) {
        model.addAttribute("employee", dao.employeeDAO().read(id));
        return "employees/edit";
    }

    @RequestMapping("employees/delete")
    public String deleteEmployee(@RequestParam int id) {
        dao.employeeDAO().delete(id);
        return "redirect:/employees/employees";
    }
}