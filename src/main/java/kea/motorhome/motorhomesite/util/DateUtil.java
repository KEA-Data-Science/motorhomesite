package kea.motorhome.motorhomesite.util;
// KCN

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Class is intended to use with the LocalDateTime class instances; most available methods compare.
 */
public class DateUtil
{
    /**
     * A period is characterized by one date/time being earlier than another:
     * Method returns true is first date in parameter list is earlier than second date in list.
     */
    public static boolean isPeriodConsistent(LocalDateTime earlierDate, LocalDateTime laterDate)
    {
        if(earlierDate == null || laterDate == null){return false;}
        return earlierDate.isBefore(laterDate);
    }

    /**
     * Method returns true if a periods, AA & AB and BA & BB, have no overlapping dates.
     */
    public static boolean doPeriodsOverlap(LocalDateTime periodAA, LocalDateTime periodAB,
                                           LocalDateTime periodBA, LocalDateTime periodBB)
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
    public static Date convertToDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.
                atZone(ZoneId.systemDefault()).
                toInstant());
    }

    public static LocalDateTime convertToLocalDateTime(Date date)
    {
        return date.
                toInstant().
                atZone(ZoneId.systemDefault()).
                toLocalDateTime();
    }

}

/**
 * Research for conversion from
 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
 */