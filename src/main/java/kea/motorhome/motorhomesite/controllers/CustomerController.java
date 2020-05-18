package kea.motorhome.motorhomesite.controllers;

import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.CustomerDAODemo;
import kea.motorhome.motorhomesite.models.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
    public class CustomerController
    {
        private IDAO customerDAO;

        public CustomerController() { customerDAO = new CustomerDAODemo(); }

        // See all customers
        @GetMapping("/customers")
        public String index(Model model){
            model.addAttribute("customers" , customerDAO.readall());
            return "customers";
        }


       // TODO: See single customer details

        @GetMapping("/customers/new")
        public String showNewCustomerForm(Model model) {
            Customer customer = new Customer();
            model.addAttribute("customer", customer);
            return "customers/new";
        }


        @RequestMapping(value ="/save", method = RequestMethod.POST)
        public String saveCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.create(customer);
            return "redirect:/customers";
        }


        @RequestMapping(value ="/update", method = RequestMethod.POST)
        public String updateCustomer(@ModelAttribute("customer") Customer customer) {
            customerDAO.update(customer);
            return "redirect:/customers";
        }


        // TODO: Edit single customer


        @RequestMapping("customers/delete")
        public String deleteCustomer(@RequestParam String id) {
            customerDAO.delete(id);
            return "redirect:/customers";
        }
    }

