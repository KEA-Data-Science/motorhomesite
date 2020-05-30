package kea.motorhome.motorhomesite.dao;

import kea.motorhome.motorhomesite.daodemo.*;
import kea.motorhome.motorhomesite.daodemo.data.DemoData;
import kea.motorhome.motorhomesite.models.*;

/**
 * Class exists to eradicate duplicate code across controllers and
 * standardize DAO access.
 */
public class SiteDAOCollection
{

    private IDAO<Address, Integer> addressDAO;
    private IDAO<Appointment, Integer> appointmentDAO;
    private IDAO<Customer, String> customerDAO;
    private IDAO<Employee, Integer> employeeDAO;
    private IDAO<Motorhome, Integer> motorhomeDAO;
    private IDAO<PayCard, Integer> paycardDAO;
    private IDAO<Period, Integer> periodDAO;
    private IDAO<Person, Integer> personDAO;
    private IDAO<Reservation, Integer> reservationDAO;
    private IDAO<Service, Integer> serviceDAO;
    private IDAO<Invoice, Integer> invoiceDAO;
    private IDAO<CarModel, Integer> carModelDAO;

    private DemoData demoData; // don't use

    private SiteDAOCollection()
    {
        /* pure demo dao */
//        this.addressDAO = new AddressDAO(); // all demos for swapping at appropriate time
//        this.appointmentDAO = new AppointmentDAODemo();
//        this.customerDAO = new CustomerDAODemo();
//        this.employeeDAO = new EmployeeDAODemo();
//        this.motorhomeDAO = new MotorhomeDAODemo();
//        this.paycardDAO = new PayCardDAODemo();
//        this.periodDAO = new PeriodDAODemo();
//        this.personDAO = new PersonDAODemo();
//        this.reservationDAO = new ReservationDAODemo();
//        this.serviceDAO = new ServiceDAODemo();
//        this.invoiceDAO = new InvoiceDAODemo();
//        this.carModelDAO = new CarModelDAODemo();


        this.addressDAO = new AddressDAO(); // all demos for swapping at appropriate time
        this.appointmentDAO = new AppointmentDAO();
        this.customerDAO = new CustomerDAO();
        this.employeeDAO = new EmployeeDAO();
        this.motorhomeDAO = new MotorhomeDAO();
        this.paycardDAO = new PayCardDAO();
        this.periodDAO = new PeriodDAO();
        this.personDAO = new PersonDAO();
        this.reservationDAO = new ReservationDAO();
        this.serviceDAO = new ServiceDAO();
        this.invoiceDAO = new InvoiceDAODemo();
        this.carModelDAO = new CarModelDAO();

    }

    private static SiteDAOCollection instance;

    /* Lazy instantiation Singleton instance*/
    public static SiteDAOCollection getInstance()
    {
        if(instance==null){instance=new SiteDAOCollection().configure();}
        return instance;
    }

    /* Method supplies demo data in the time of Demos, devourer of Hours. */
    private SiteDAOCollection configure()
    {
//        demoData = new DemoData(this); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return this;
    }

    public IDAO<Address, Integer> addressDAO(){ return addressDAO; }

    public IDAO<Appointment, Integer> appointmentDAO(){ return appointmentDAO; }

    public IDAO<Customer, String> customerDAO(){ return customerDAO; }

    public IDAO<Employee, Integer> employeeDAO(){ return employeeDAO; }

    public IDAO<Motorhome, Integer> motorhomeDAO(){ return motorhomeDAO; }

    public IDAO<PayCard, Integer> paycardDAO(){ return paycardDAO; }

    public IDAO<Period, Integer> periodDAO(){ return periodDAO; }

    public IDAO<Person, Integer> personDAO(){ return personDAO; }

    public IDAO<Reservation, Integer> reservationDAO(){ return reservationDAO; }

    public IDAO<Service, Integer> serviceDAO(){ return serviceDAO; }

    public IDAO<Invoice, Integer> invoiceDAO(){return invoiceDAO;}

    public IDAO<CarModel, Integer> carModelDAO(){return carModelDAO;}

}
