package kea.motorhome.motorhomesite.util;
// by LNS
import kea.motorhome.motorhomesite.dao.SiteDAOCollection;
import kea.motorhome.motorhomesite.models.CarModel;
import kea.motorhome.motorhomesite.models.Motorhome;
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.models.Service;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculateTotalPriceOfInvoice() {
    }

    // Testing calculatePriceOfPeriod()
    @Test
    void motorhomeAndPeriodAreEmpty() {
        // Arrange
        Motorhome motorhome = new Motorhome();
        Period period = new Period();
        // Act & Assert
        assertEquals(0, priceCalculator.calculatePriceOfPeriod(motorhome,period)); // Motorhome and period can be empty
    }

    // Testing calculatePriceOfPeriod()
    @Test
    void periodDatesAreReversed() {
        // Arrange
        Motorhome motorhome = new Motorhome();
        Period period = new Period();
        LocalDate start = LocalDate.of(2020,6,1);
        LocalDate end = LocalDate.of(2020,1,1);
        period.setStart(start);
        period.setEnd(end);
        // Act & Assert
        assertEquals(0, priceCalculator.calculatePriceOfPeriod(motorhome,period)); // No error when period dates are in wrong order?
    }

    // Testing calculatePriceOfService()
    @Test
    void listOfServicesIsEmpty() {
        // Arrange
        List<Service> services = new ArrayList<>();
        // Act & Assert
        assertEquals(0, priceCalculator.calculatePriceOfService(services)); // An empty list is possible
    }

    // Testing calculatePriceOfService()
    @Test
    void priceOfServiceIsNegative() {
        // Arrange
        List<Service> services = new ArrayList<>();
        Service service1 = new Service(1, "Cleaning", -100, "Cleaning");
        services.add(service1);
        // Act & Assert
        assertEquals(-100, priceCalculator.calculatePriceOfService(services)); // Service prices can be negative
    }

    // Testing calculatePriceOfService()
    @Test
    void priceOfServiceHasNotBeenSet() {
        // Arrange
        List<Service> services = new ArrayList<>();
        Service service1 = new Service();
        services.add(service1);
        // Act & Assert
        assertEquals(0, priceCalculator.calculatePriceOfService(services)); // Service does not need to have a price
    }

    @Test
    void getDateUtil() {
    }
}