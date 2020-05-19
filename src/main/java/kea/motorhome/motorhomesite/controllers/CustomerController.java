package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.CustomerDAODemo;
import kea.motorhome.motorhomesite.daodemo.MotorhomeDAODemo;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Customer;
import kea.motorhome.motorhomesite.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
    public class CustomerController
    {
        private IDAO customerDAO;

        public CustomerController() { customerDAO = new CustomerDAODemo(); } // Notice if demo or actual DAO

        @GetMapping("/customers")
        public String index(Model model){
            model.addAttribute("customers" , customerDAO.readall());
            return "customers";
        }

        @GetMapping("/customers/details")
       public String getCustomerByParameter(Model model, @RequestParam String id) {
            // Customer customer = customerDAO.read(id); // Laurits: Why does this not work, when the line below does?
            Customer customer = new CustomerDAODemo().read(id); // Notice if DAODemo or actual DAO
           model.addAttribute("customer", customer);
           return "customers/details";
       }

        @GetMapping("/customers/new")
        public String showNewCustomerForm(Model model) {
            Customer customer = new Customer();
            Person person = new Person();
            Address address = new Address();
            model.addAttribute("customer", customer);
            model.addAttribute("person", person);
            model.addAttribute("address", address);
            return "customers/new";
        }

        @RequestMapping(value ="/save", method = RequestMethod.POST)
        public String createCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.create(customer);
            return "redirect:/customers";
        }

        @RequestMapping(value ="/update", method = RequestMethod.POST)
        public String updateCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.update(customer);
            return "redirect:/customers";
        }

        @GetMapping("/customers/edit")
        public String showEditCustomerForm(Model model, @RequestParam String id) {
            Customer customer = new CustomerDAODemo().read(id); // Notice if DAODemo or actual DAO
            model.addAttribute("customer", customer);
            return "customers/edit";
        }

        @RequestMapping("customers/delete")
        public String deleteCustomer(@RequestParam String id) {
            customerDAO.delete(id);
            return "redirect:/customers";
        }
    }