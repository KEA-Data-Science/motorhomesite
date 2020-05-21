package kea.motorhome.motorhomesite.daodemo.data;

import kea.motorhome.motorhomesite.dao.ServiceDAO;
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.daodemo.MotorhomeDAODemo;
import kea.motorhome.motorhomesite.enums.SiteRole;
import kea.motorhome.motorhomesite.models.*;

import java.time.LocalDate;
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
        Service service1 = new Service(0, "Cleaning", 20, "Cleaning");
        Service service2 = new Service(1, "Repairing", 100, "Repairing");
        Service service3 = new Service(2, "Extra space", 45, "Extra space");
        dao.serviceDAO().readall().add(service1);
        dao.serviceDAO().readall().add(service2);
        dao.serviceDAO().readall().add(service3);

        /* Customers */

        Address address1 = new Address(1, "Danmark", "Margovej", "15B", "5432");
        Person person1 = new Person(1, "Alfred", "Boolan",
                                    address1, LocalDate.now().minusYears(2), "aBool@mail.com",
                                    SiteRole.CUSTOMER, LocalDate.now());
        System.out.println("1111-1234-1234 was added");
        dao.customerDAO().readall().add(new Customer("1111-1234-1234", 1, person1, true));

        Address address2 = new Address(2, "Danmark", "Vej", "Nr.", "2332");
        Person person2 = new Person(2, "Bubbi", "Bipbup", address2, LocalDate.now().minusYears(4),
                                    "buBool@mail.com", SiteRole.CUSTOMER,
                                    LocalDate.now());
        dao.customerDAO().readall().add(new Customer("1234-1234-1235", 2, person2, true));

        /* Invoices */
        Invoice invoice1 = new Invoice(1, "1234-1234-1235", new Period(LocalDate.of(2020, 5, 1),
                LocalDate.of(2020, 5, 22)), dao.serviceDAO().readall());

        Invoice invoice2 = new Invoice(2, "1234-1234-1235", new Period(LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 21)), dao.serviceDAO().readall());

        Invoice invoice3  = new Invoice(3, "1234-1234-1235", new Period(LocalDate.of(2020, 2, 2),
                LocalDate.of(2020, 3, 5)), dao.serviceDAO().readall());

        dao.invoiceDAO().readall().add(invoice1);
        dao.invoiceDAO().readall().add(invoice2);
        dao.invoiceDAO().readall().add(invoice3);

        /*motorhomes*/
        List<Service> servicesAvailable = dao.serviceDAO().readall();


        dao.motorhomeDAO().readall().add(new Motorhome(1, new CarModel("Big One Runnrunn", "UHYTG4334", 120, 3), "15-14-AH4",
                                      new float[]{230, 270, 380}, "This wonderful vehicle can take you anywhere as long as it is not uphill" +
                                                                  ".", "/img/MH1.jpg", servicesAvailable,
                                      2008, "One of the few seven wheeled vehicles that make the cut.",
                                      5, "R3COH"));
        dao.motorhomeDAO().readall().add(new Motorhome(2, new CarModel("Busty Deep Seater", "UHYTG4564", 145, 4), "15-14-AH4",
                                      new float[]{260, 300, 460}, "This wonderful vehicle can take you " +
                                                                  "anywhere as long as it is not downhill" +
                                                                  ".", "/img/MH2.jpg", servicesAvailable,
                                      2008, "Unlimited that make the cut.",
                                      5, "H2O"));
        dao.motorhomeDAO().readall().add(new Motorhome(3, new CarModel("Normal Motor Home", "UHYTG4564", 145, 4), "15-14-AH4",
                                      new float[]{260, 300, 460}, "This vehicle is hilarious" +
                                                                  " but only on flat terrain." +
                                                                  ".", "/img/MH3.jpg", servicesAvailable,
                                      2008, "Unlimited that make the cut.",
                                      5, "Joy"));
        dao.motorhomeDAO().readall().add(new Motorhome(4, new CarModel("Abnormal Motor Home", "UHYTG6565", 202, 42), "15-14" +
                                                                                                    "-AH4",
                                      new float[]{1320, 1660, 2560}, "This vehicle is hilarious" +
                                                                     " but only on flat terrain." +
                                                                     ".", "/img/MH3.jpg", servicesAvailable,
                                      2008, "Unlimited that make the cut.",
                                      5, "Money"));

    }
}
