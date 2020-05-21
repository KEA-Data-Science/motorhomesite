package kea.motorhome.motorhomesite;

import kea.motorhome.motorhomesite.models.Invoice;
import kea.motorhome.motorhomesite.models.Motorhome;
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.models.Service;
import kea.motorhome.motorhomesite.util.DateUtil;

import java.time.LocalDate;

public class PriceCalculator
{
    private DateUtil dateUtil;

    public PriceCalculator()
    {
        this.dateUtil = new DateUtil();
    }

    public float calculatePriceOfPeriod(Motorhome motorhome, Period period)
    {
        /* Testy stuff */

        System.out.println("Calculating period price, taking seasons into account");
        System.out.println(motorhome);
        System.out.println(period);


        float result = 0;

        LocalDate tempDate = period.getStart().withYear(2000); // because of auto-initialized DateUtil

        int tempYearTakingLeapIntoAccount = DateUtil.determineIfLeapYear(
                period.getEnd().plusDays(1)) ? 2000 : 2001;

        while(tempDate.isBefore(period.getEnd().withYear(tempYearTakingLeapIntoAccount)))
        {
            String season = dateUtil.determineSeasonOfDate(tempDate);
            System.out.println("Season of " + tempDate + "\tis\t" + season);
            switch(season)
            {
                case "Low":
                    result += motorhome.getSeasonalDailyCharge()[0];
                    break;           // 0 is low-season
                case "Medium":
                    result += motorhome.getSeasonalDailyCharge()[1];
                    break;           // 2 is low-season
                case "High":
                    result += motorhome.getSeasonalDailyCharge()[2];
                    break;         // 3 is low-season
            }

            System.out.println("Running total is:\t " + result + "\n");

            tempDate = tempDate.plusDays(1);
        }
        System.out.println("Total days of rental : " + period.duration()[0] + " dage \t" +
                           period.duration()[1] + " måneder" +
                           period.duration()[2] + " år"
                          );
        System.out.println("Returned total amount for period: " + result);

        return result;
    }

    public float calculateTotalPriceOfInvoice(Invoice invoice)
    {
        float totalPrice = calculatePriceOfPeriod(invoice.getMotorhome(), invoice.getBillPeriod());

        for (Service service: invoice.getServices())
        {
            totalPrice += service.getUnitPrice();
        }

        return totalPrice;
    }

    public DateUtil getDateUtil(){ return dateUtil; }

    public void setDateUtil(DateUtil dateUtil){ this.dateUtil = dateUtil; }

}
