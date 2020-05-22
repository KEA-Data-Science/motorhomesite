package kea.motorhome.motorhomesite.util;
// KCN

import kea.motorhome.motorhomesite.models.Period;

import java.time.LocalDate;
//import java.time.Period; // wow - den klasse skal også bruges
import java.util.Date;


/**
 * Class is intended to use with the LocalDateTime class instances; most available methods compare.
 */
public class DateUtil
{

    /**
     * Parameterless constructor; demands attention
     * Periods recorded with year 2000;
     */

    boolean autoInitializedSeasion;
    /// the splitting of three seasons into six has to do with the shape of a year
    private Period lowSeasonA;
    private Period mediumSeasonA;
    private Period highSeason;
    private Period mediumSeasonB;
    private Period lowSeasonB;

    public DateUtil()
    {
        lowSeasonA = new Period(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 26));
        mediumSeasonA = new Period(LocalDate.of(2000, 2, 27), LocalDate.of(2000, 4, 15));
        highSeason = new Period(LocalDate.of(2000, 4, 16), LocalDate.of(2001, 8, 15));
        mediumSeasonB = new Period(LocalDate.of(2000, 8, 16), LocalDate.of(2001, 10, 15));
        lowSeasonB = new Period(LocalDate.of(2000, 10, 1), LocalDate.of(2000, 12, 31));

        autoInitializedSeasion = true;
    }

    public DateUtil(Period lowSeasonA, Period mediumSeasonA, Period highSeason, Period mediumSeasonB, Period lowSeasonB)
    {
        autoInitializedSeasion = false;

        this.lowSeasonA = lowSeasonA;
        this.mediumSeasonA = mediumSeasonA;
        this.highSeason = highSeason;
        this.mediumSeasonB = mediumSeasonB;
        this.lowSeasonB = lowSeasonB;
    }

    /**
     * A period is characterized by one date/time being earlier than another:
     * Method returns true is first date in parameter list is earlier than second date in list.
     */
    public static boolean isPeriodConsistent(LocalDate earlierDate, LocalDate laterDate)
    {
        if(earlierDate == null || laterDate == null){return false;}
        return earlierDate.isBefore(laterDate);
    }

    /**
     * Method returns true if a periods, AA & AB and BA & BB, have no overlapping dates.
     */
    public static boolean doPeriodsOverlap(LocalDate periodAA, LocalDate periodAB,
                                           LocalDate periodBA, LocalDate periodBB)
    {
        // null check, with return true if null found; true usually indicates a no-go situation when
        if(periodAA == null || periodAB == null || periodBA == null || periodBB == null)
        {
            throw new NullPointerException("From DateUtil::isPeriodConsistent : You supplied a null object." +
                                           " Please fix the whole");
        }

        boolean aIsEarlier = periodAB.isBefore(periodBA);// check if a ends earlier than b begins
        boolean bIsEarlier = periodBB.isBefore(periodAA);// check if b ends is earlier than a begins
        // in either case return false, else return true (meaning they overlap)
        return !(aIsEarlier || bIsEarlier);
    }

    /**
     * Method returns a Date object by converting a LocalDateTime object to an Instance
     * object (time zone set to system default) and converting this Instance object
     * into a Date object.
     */
    public static Date convertToDate(LocalDate localDateTime)
    {
        return java.sql.Date.valueOf(localDateTime);
    }

    /**
     * up for deletion
     */
    public static LocalDate convertToLocalDate(java.sql.Date date)
    {
        return date.toLocalDate();
    }

    public String determineSeasonOfDate(LocalDate date)
    {
        /* the temp date is there to make sure nothing happens to the supplied date object */
        LocalDate temp;
        int tempYear;

        if(autoInitializedSeasion)
        { /* if DateUtil is auto initialized, we need to adjust all dates for leap year  */
            setSeasonDatesBasedOnLeapYear(date);
            tempYear = determineIfLeapYear(date) ? 2000 : 2001;
        } else
        {
            tempYear = date.getYear();
        }
        /* if instance is auto initialized, we adjust the year of the new LocalDate object */
        temp = autoInitializedSeasion ? LocalDate.from(date).withYear(tempYear) : LocalDate.from(date);

        if(lowSeasonA.dateOverlapsWithPeriod(temp)){ return "Low"; }
        if(mediumSeasonB.dateOverlapsWithPeriod(temp)){ return "Medium"; }
        if(highSeason.dateOverlapsWithPeriod(temp)){ return "High"; }
        if(mediumSeasonB.dateOverlapsWithPeriod(temp)){ return "Medium"; }
        if(lowSeasonA.dateOverlapsWithPeriod(temp)){ return "Low"; }

        // in case we reach here, the safe assumption is Medium Season
        return "Medium";
    }

    private void setSeasonDatesBasedOnLeapYear(LocalDate date)
    {
        if(determineIfLeapYear(date))
        { /* 2000 was a leap year, so a temporary date is set with the year 2000 */
            setSeasonYear(2000);
        } else
        {
          /*since auto initialization was done, setting the year to 2000, adjustments need to be made
           for all season periods*/
            setSeasonYear(2001);
        }
    }

    /**
     * Method determines if given date is in a leap year
     *
     * @ returns true if leap year.
     */
    public static boolean determineIfLeapYear(LocalDate date)
    {
        int year = date.getYear();
        return ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    }

    public void setSeasonYear(int year)
    {
        lowSeasonA.setStart(lowSeasonA.getStart().withYear(year));
        mediumSeasonA.setStart(mediumSeasonA.getStart().withYear(year));
        highSeason.setStart(highSeason.getStart().withYear(year));
        mediumSeasonB.setStart(mediumSeasonB.getStart().withYear(year));
        lowSeasonB.setStart(lowSeasonB.getStart().withYear(year));

        lowSeasonA.setEnd(lowSeasonA.getEnd().withYear(year));
        mediumSeasonA.setEnd(mediumSeasonA.getEnd().withYear(year));
        highSeason.setEnd(highSeason.getEnd().withYear(year));
        mediumSeasonB.setEnd(mediumSeasonB.getEnd().withYear(year));
        lowSeasonB.setEnd(lowSeasonB.getEnd().withYear(year));
    }

    /**
     * Returns the current date (system local time)
     */
    public LocalDate getToday(){return LocalDate.now();}

}

/*
 * Research for conversion from
 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
 * And the beautiful line, calculating leap year:
 * https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year
 */