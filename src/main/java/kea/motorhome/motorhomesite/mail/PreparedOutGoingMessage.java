package kea.motorhome.motorhomesite.mail;

import kea.motorhome.motorhomesite.models.Reservation;
import kea.motorhome.motorhomesite.util.PriceCalculator;
/** Class contains - to NMH very tightly coupled - methods that all send back a String
 * intended to be sent as an email to a customer. */
public class PreparedOutGoingMessage
{

    public String getReservationConfirmationEmailText(Reservation reservation){
        PriceCalculator priceCalculator = new PriceCalculator();

        String message =
                "Thank you so much, " + reservation.getCustomer().getPerson().getFullName() + "!\n\n" +
                "We are absolutely sure that you will have a wonderful time with the " +
                reservation.getMotorhome().getModel().getModelName() + "." +
                "\n\n\tPeriod booked:\t" + reservation.getPeriod().getStart() + "\tto\t" + reservation.getPeriod().getEnd() +
                "\n" +
                "Please be aware of demonic cancellation fees. We own your house now.\n\n";

        float periodPrice = priceCalculator.calculatePriceOfPeriod(reservation.getMotorhome(), reservation.getPeriod());
        float servicesPrice = priceCalculator.calculatePriceOfService(reservation.getServices());

        message += "Base price of rental period:\t"
                   + periodPrice + " kr\n" +
                   "Price of services:\t\t\t" + servicesPrice + " kr." +
                   "\nTOTAL AMOUNT OWED:\t" + (periodPrice + servicesPrice) + " kr";

        message += "\n\nYour invoice will be sent to this address ~" + reservation.getPeriod().getStart().minusWeeks(2)+
                   "\nand with all haste if this date has already passed.\n\n";

        message += "Contact:\n" +
                   "You are welcome to contact us one this number: 555-2323-1\n" +
                   reservation.getEmployee().getPerson().getFirstName() +
                   " " + reservation.getEmployee().getPerson().getLastName().substring(0,1) +
                   " knows the details of your reservation and is there to help.\n\n" +
                   "You can email"+ reservation.getEmployee().getPerson().getFirstName()
                   + " me any question @ "+ reservation.getEmployee().getPerson().getEmail() +" " +
                   "\n\n" ;


        message +=
                "You rock,\n" +
                "Thank,\n" +
                "Nordic Motorhome Rental";

        message += "\n----------------------------------------------\n" +
                   "\nGeneral Terms:\n\n" +
                   "•\tThe price per day includes 400 free kilometers per day. \n" +
                   "•\tInsurance is included in the price.\n" +
                   "•\tExternal cleaning is included in the price.\n" +
                   "\n" +
                   "Pick-up and drop-off points\n" +
                   "Nordic motorhome rental offers delivery of motorhomes to a location that is most " +
                   "convenient for the customers.\n" +
                   " It is often an airport, but it can be any other location for instance a ferry port or " +
                   "a hotel.\n " +
                   "For pick-up or drop-off outside Nordic motorhome Rentals office, customers will be " +
                   "charged\n" +
                   " a transfer cost of 0,70€ per kilometer.\n\n" +
                   "Extras\n" +
                   "To make the trip more enjoyable customers can rent accessories such as bike rack, bed " +
                   "linen,\n" +
                   " child seat, picnic table and chairs etc.\n\n" +
                   "Fuel & driven kilometers\n" +
                   "When customers picks-up the motorhome the tank is full. By drop-off the staff checks the fuel level" +
                   "\n and reads kilometers of the speedometer. If the tank it is less than ½ full there " +
                   "will be a charge of 70€.\n If it turns out that the customer in average has driven more" +
                   " " +
                   "than 400 kilometers\n per day the customer will be charged 1€ per extra kilometer.\n\n" +
                   "Cancellation\n" +
                   "In case of cancellation following charges become due:\n" +
                   "•\t    Up to 50 days prior to the start of the term of rental: 20% of the rental price, minimum 200€\n" +
                   "•\t    Between 49 and 15 days prior to the start of the term of rental: 50% of the rental price\n" +
                   "•\t    Less than 15 days prior to the start of the term of rental: 80% of the rental price\n" +
                   "•\tOn the day of renting: 95% of the rental price\n" +
                   "\n" +
                   "Service and repairs\n" +
                   "After drop-off the Motorhome will be cleaned. The auto mechanic checks oil & water etc." +
                   "\nIf repairs or further service is needed the auto mechanic register this.\n";

        return message;
    }

}
