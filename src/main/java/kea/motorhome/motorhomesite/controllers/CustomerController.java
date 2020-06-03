package kea.motorhome.motorhomesite.controllers;
// by LNS, kcn
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.Address;
import kea.motorhome.motorhomesite.models.Customer;
import kea.motorhome.motorhomesite.models.PayCard;
import kea.motorhome.motorhomesite.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController
{

    public CustomerController() { }

    private SiteDAOCollection dao(){return SiteDAOCollection.getInstance();}

    @GetMapping("/customers/customers")
    public String customersPage(Model model)
    {
        model.addAttribute("customers", dao().customerDAO().readall());
        return "customers/customers";
    }

    @GetMapping("/customers/details")
    public String getCustomerByParameter(Model model, @RequestParam String id)
    {
        model.addAttribute("customer", dao().customerDAO().read(id));
        return "customers/details";
    }

    @GetMapping("/customers/new")
    public String showNewCustomerForm(Model model)
    {
        Customer customer = new Customer();
        customer.setCustomerID(dao().customerDAO().readall().size()+1);

        Person person = new Person();
        person.setPersonID(dao().personDAO().readall().size()+1);
        customer.setPerson(person);

        Address address = new Address();
        address.setAddressID(dao().addressDAO().readall().size()+1);
        customer.getPerson().setAddress(address);

        PayCard payCard = new PayCard();
        payCard.setCardID(dao().paycardDAO().readall().size()+1);
        customer.setPayCard(payCard);

        model.addAttribute("customer", customer);
        model.addAttribute("person", person);
        model.addAttribute("address", address);
        model.addAttribute("payCard", payCard);
        return "customers/new";
    }

    @RequestMapping(value = "/savecustomer", method = RequestMethod.POST)
    public String createCustomer(@ModelAttribute("customer") Customer customer,
                                 @ModelAttribute("person") Person person,
                                 @ModelAttribute("address") Address address,
                                 @ModelAttribute("payCard") PayCard payCard)
    {
        /*
        System.out.println("From CUSTOMERCONTROLLER::createCustomer\n" +
                           "" + customer+ "\n"+
                           "" + person + "\n"+
                           "" + address + "\n" +
                           "" + payCard + "\n"
                          ); // dataen bliver ikke ført med fra formuleren
                          */

        customer.getPerson().setUserType(SiteRole.CUSTOMER);

        dao().paycardDAO().create(customer.getPayCard());
        dao().addressDAO().create(customer.getPerson().getAddress());
        dao().personDAO().create(customer.getPerson());
        dao().customerDAO().create(customer);
        return "redirect:/customers/customers";
    }

    @RequestMapping(value = "/updatecustomer", method = RequestMethod.POST)
    public String updateCustomer(@ModelAttribute("customer") Customer customer)
    {
        dao().customerDAO().update(customer);
        dao().personDAO().update(customer.getPerson());
        dao().addressDAO().update(customer.getPerson().getAddress());
        dao().paycardDAO().update(customer.getPayCard());

        return "redirect:/customers/customers";
    }

    @GetMapping("/customers/edit")
    public String showEditCustomerForm(Model model, @RequestParam String id)
    {
        Customer customer = dao().customerDAO().read(id);

        if(customer!=null)
        {
            model.addAttribute("customer", customer);
            model.addAttribute("person", customer.getPerson());
            model.addAttribute("address", customer.getPerson().getAddress());
            model.addAttribute("payCard", customer.getPayCard());
            return "customers/edit";
        }

        model.addAttribute("feedbackMessage","The customer (id " + id + ") you are trying to edit" +
                                             "was not found in the system.");
        return "feedback";
    }

    @RequestMapping("customers/delete")
    public String deleteCustomer(@RequestParam String id)
    {
        dao().customerDAO().delete(id);
        return "redirect:/customers/customers";
    }
}