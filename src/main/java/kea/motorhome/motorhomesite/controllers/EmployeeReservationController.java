package kea.motorhome.motorhomesite.controllers;


import kea.motorhome.motorhomesite.dao.IDAO;
import kea.motorhome.motorhomesite.daodemo.ReservationDAODemo;
import kea.motorhome.motorhomesite.models.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // annotation
public class EmployeeReservationController
{
    private IDAO<Reservation, Integer> reservationDAO;

    public EmployeeReservationController(){
        reservationDAO = new ReservationDAODemo(); // notice demo DAO
    }

    @GetMapping("/reservation/create")
    @ResponseBody
    public String employeeMakeReservation()
    {
        return "You followed /reservation/create";
    }

    @GetMapping("/reservation/read")
    @ResponseBody
    public String readReservation(@RequestParam String id)
    {
        return "You followed /reservation/read " + id;
    }

}
