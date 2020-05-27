package kea.motorhome.motorhomesite.daodemo.data;

import kea.motorhome.motorhomesite.dao.ServiceDAO;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.MotorhomeDAODemo;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DemoData
{
    /* eager instantiated singleton to ensure program has demo data */
    public DemoData(SiteDAOCollection dao)
    {
        renderDemoData(dao);
    }

    private void renderDemoData(SiteDAOCollection dao)
    {

        /*services*/
        Service service2 = new Service(2, "Repairing", 100, "Repairing");
        Service service3 = new Service(3, "Extra space", 45, "Extra space");
        Service service1 = new Service(1, "Cleaning", 88, "Cleaning");

        dao.serviceDAO().readall().add(service2);
        dao.serviceDAO().readall().add(service3);
        dao.serviceDAO().readall().add(service1);


        /* Addresses */
        Address address1 = new Address(1, "Danmark", "Margovej", "15B", "5432");
        Address address2 = new Address(2, "Danmark", "Vej", "Nr.", "2332");
        Address address3 = new Address(3, "Danmark", "Kastrupvej","15B","1900");
        Address address4 = new Address(4, "Danmark", "Brønshøjvej","Nr 1.","2600");
        Address address5 = new Address(5,"Danmark","Østergade", "1", "3700");
        Address address6 = new Address(6,"Danmark","Ai Wei Way", "2", "8000");

        dao.addressDAO().readall().add(address1);
        dao.addressDAO().readall().add(address2);
        dao.addressDAO().readall().add(address3);
        dao.addressDAO().readall().add(address4);
        dao.addressDAO().create(address5);
        dao.addressDAO().create(address6);


        /* PayCards */
        PayCard payCard1 = new PayCard(1,"Visa Elektron", "1111-1111-1111-1111", LocalDate.now().plusYears(1),1111);
        PayCard payCard2 = new PayCard(2,"Visa Infinite", "2222-2222-2222-2222", LocalDate.now().plusYears(2),2222);
        dao.paycardDAO().readall().add(payCard1);
        dao.paycardDAO().readall().add(payCard2);

        /* Persons */
        Person person1 = new Person(1, "Alfred", "Boolan",
                address1, LocalDate.now().minusYears(2), "aBool@maiiiil.com",
                SiteRole.CUSTOMER, LocalDate.now(),"password");
        Person person2 = new Person(2, "Bubbi", "Bipbup", address2, LocalDate.now().minusYears(4),
                "buBool@maul.com", SiteRole.CUSTOMER,
                LocalDate.now(),"password");
        Person person3 = new Person(3, "Anna", "Adminsen", address3, LocalDate.now().minusYears(2),
                "anna@nmh.dk",
                SiteRole.ADMIN,
                LocalDate.now(),"password");
        Person person4 = new Person(4, "Søren", "Sælgersen", address4, LocalDate.now().minusYears(4),
                "sørensælger@trixxxster.org",
                SiteRole.SALES,
                LocalDate.now(),"password");
        dao.personDAO().readall().add(person1);
        dao.personDAO().readall().add(person2);
        dao.personDAO().readall().add(person3);
        dao.personDAO().readall().add(person4);

        /* Customers */
        Customer customer1 = new Customer ("1111-1234-1234", payCard1, person1, true);
        Customer customer2 = new Customer ("1234-1234-1235", payCard2, person2, true);
        dao.customerDAO().readall().add(customer1);
        dao.customerDAO().readall().add(customer2);

        /* Employees */
        Employee employee1 = new Employee(1, person3,1);
        Employee employee2 = new Employee(2, person4,2);
        dao.employeeDAO().readall().add(employee1);
        dao.employeeDAO().readall().add(employee2);

        /*motorhomes*/
        List<Service> servicesAvailable = dao.serviceDAO().readall();

        CarModel carModel1 = new CarModel("Big One Runnrunn", "UHYTG4334", 120, 3, 20, 4.5f, 2.7f, 2.2f,
                1500, 25, 30, 5, true, true, true);
        CarModel carModel2 = new CarModel("Busty Deep Seater", "UHYTG4564", 145, 4, 25, 4.1f, 3.0f, 2.5f,
                2000, 27, 29, 4, true, true, false);
        CarModel carModel3 = new CarModel("Normal Motor Home", "UHYTG4564", 145, 4, 30, 3.4f, 3.1f, 2.4f,
                1700, 30, 40, 6, true, false, true );
        CarModel carModel4 = new CarModel("Abnormal Motor Home", "UHYTG6565", 202, 4, 40, 4.3f, 3.2f, 2.8f,
                3000, 35, 50, 5, false, true, true);

        dao.motorhomeDAO().readall().add(new Motorhome(1, carModel1, "15-14-AH4",
                new float[]{230, 270, 380}, "This wonderful vehicle can take you anywhere as long as it is not uphill" +
                ".", "/img/MH1.jpg", servicesAvailable,
                2008, "One of the few seven wheeled vehicles that make the cut.",
                5, "R3COH"));
        dao.motorhomeDAO().readall().add(new Motorhome(2, carModel2, "15-14-AH4",
                new float[]{260, 300, 460}, "This wonderful vehicle can take you " +
                "anywhere as long as it is not downhill" +
                ".", "/img/MH2.jpg", servicesAvailable,
                2008, "Unlimited that make the cut.",
                5, "H2O"));
        dao.motorhomeDAO().readall().add(new Motorhome(3, carModel3, "15-14-AH4",
                new float[]{260, 300, 460}, "This vehicle is hilarious" +
                " but only on flat terrain." +
                ".", "/img/MH3.jpg", servicesAvailable,
                2008, "Unlimited that make the cut.",
                5, "Joy"));
        dao.motorhomeDAO().readall().add(new Motorhome(4, carModel4, "15-14" +
                "-AH4",
                new float[]{1320, 1660, 2560}, "This vehicle is hilarious" +
                " but only on flat terrain." +
                ".", "/img/MH3.jpg", servicesAvailable,
                2008, "Unlimited that make the cut.",
                5, "Money"));

        /* Invoices */

        List<Service> serviceList1 = new ArrayList<>();
        List<Service> serviceList2 = new ArrayList<>();
        List<Service> serviceList3 = new ArrayList<>();


        serviceList1.add(service2);
        serviceList1.add(service3);
        serviceList1.add(service1);

        serviceList2.add(service1);
        serviceList2.add(service2);

        serviceList3.add(service3);

        Invoice invoice1 = new Invoice(1, "1234-1234-1235", new Period(LocalDate.of(2020, 5, 1),
                LocalDate.of(2020, 5, 22)), dao.motorhomeDAO().read(1), serviceList1, true);

        Invoice invoice2 = new Invoice(2, "1234-1234-1235", new Period(LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 21)), dao.motorhomeDAO().read(2), serviceList2, true);

        Invoice invoice3  = new Invoice(3, "1111-1234-1234", new Period(LocalDate.of(2020, 2, 2),
                LocalDate.of(2020, 3, 5)), dao.motorhomeDAO().read(3),serviceList3, true );

        dao.invoiceDAO().readall().add(invoice1);
        dao.invoiceDAO().readall().add(invoice2);
        dao.invoiceDAO().readall().add(invoice3);

        /* Appointments */
        // Adds list of employees
        List<Integer> employeeIDs1 = new ArrayList<>();
        employeeIDs1.add(1);
        employeeIDs1.add(2);
        List<Integer> employeeIDs2 = new ArrayList<>();
        employeeIDs2.add(1);
        // Creates appointments
        Appointment appointment1 = new Appointment(LocalDate.now().plusYears(1), LocalTime.now(), address5, 1, employeeIDs1, "Pick-up", 1, 200);
        Appointment appointment2 = new Appointment(LocalDate.now().plusYears(3), LocalTime.now(), address6, 2, employeeIDs2, "Drop-off", 2, 300);
        // Adds to dao list
        dao.appointmentDAO().create(appointment1);
        dao.appointmentDAO().create(appointment2);
    }
}
