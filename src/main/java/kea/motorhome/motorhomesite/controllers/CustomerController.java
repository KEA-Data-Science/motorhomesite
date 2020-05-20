package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.CustomerDAODemo;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Customer;
import kea.motorhome.motorhomesite.models.Person;
import kea.motorhome.motorhomesite.models.Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
    public class CustomerController
    {
        private IDAO customerDAO;

        public CustomerController() { customerDAO = new CustomerDAODemo(); } // Notice if demo or actual DAO

        @GetMapping("/customers/customers")
        public String customersPage(Model model){
            model.addAttribute("customers" , customerDAO.readall());
            return "customers/customers";
        }

        @GetMapping("/customers/details")
       public String getCustomerByParameter(Model model, @RequestParam String id) {
           model.addAttribute("customer", customerDAO.read(id));
           return "customers/details";
       }

        @GetMapping("/customers/new")
        public String showNewCustomerForm(Model model) {
            Customer customer = new Customer();
            Person person = new Person();
            customer.setPerson(person);
            Address address = new Address();
            customer.getPerson().setAddress(address);
            model.addAttribute("customer", customer);
            model.addAttribute("person", person);
            model.addAttribute("address", address);
            return "customers/new";
        }

        @RequestMapping(value ="/savecustomer", method = RequestMethod.POST)
        public String createCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.create(customer);
            return "redirect:/customers/customers";
        }

        @RequestMapping(value ="/updatecustomer", method = RequestMethod.POST)
        public String updateCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.update(customer);
            return "redirect:/customers/customers";
        }

        @GetMapping("/customers/edit")
        public String showEditCustomerForm(Model model, @RequestParam String id) {
            model.addAttribute("customer", customerDAO.read(id));
            return "customers/edit";
        }

        @RequestMapping("customers/delete")
        public String deleteCustomer(@RequestParam String id) {
            customerDAO.delete(id);
            return "redirect:/customers/customers";
        }
    }