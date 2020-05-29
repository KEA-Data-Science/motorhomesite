package kea.motorhome.motorhomesite.util;
// kcn
import kea.motorhome.motorhomesite.models.Invoice;
import kea.motorhome.motorhomesite.models.Motorhome;
import kea.motorhome.motorhomesite.models.Period;
import kea.motorhome.motorhomesite.models.Service;
import kea.motorhome.motorhomesite.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

public class PriceCalculator
{
    private DateUtil dateUtil;

    public PriceCalculator()
    {
        this.dateUtil = new DateUtil();
    }

    public float calculateTotalPriceOfInvoice(Invoice invoice)
    {
        float totalPrice = calculatePriceOfPeriod(invoice.getMotorhome(), invoice.getReservationPeriod());

        totalPrice += calculatePriceOfService(invoice.getServices());

        return totalPrice;
    }

    public float calculatePriceOfPeriod(Motorhome motorhome, Period period)
    {

        float result = 0;

        LocalDate tempDate = period.getStart().withYear(2000); // because of auto-initialized DateUtil

        int tempYearTakingLeapIntoAccount = DateUtil.determineIfLeapYear(
                period.getEnd().plusDays(1)) ? 2000 : 2001;

        while(tempDate.isBefore(period.getEnd().withYear(tempYearTakingLeapIntoAccount)))
        {
            String season = dateUtil.determineSeasonOfDate(tempDate);

            switch(season)
            {
                case "Low":
                    result += motorhome.getSeasonalDailyCharge()[0];
                    break;           // 0 is low-season
                case "Medium":
                    result += motorhome.getSeasonalDailyCharge()[1];
                    break;           // 1 is low-season
                case "High":
                    result += motorhome.getSeasonalDailyCharge()[2];
                    break;         // 2 is low-season
            }

            tempDate = tempDate.plusDays(1);
        }

        return result;
    }

    public float calculatePriceOfService(List<Service> services)
    {
        float result = 0f;
        for(Service s : services) { result += s.getUnitPrice(); }
        return result;
    }

    public DateUtil getDateUtil(){ return dateUtil; }
}
