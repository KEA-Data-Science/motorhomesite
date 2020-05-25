package kea.motorhome.motorhomesite.models;
// KCN
import kea.motorhome.motorhomesite.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date; // java.util.Dates can be used with class because
import java.util.Objects;

/**
 * A period object wraps two LocalDateTime objects, and exposes instance methods that
 * allow comparison of this and other periods.
 */
public class Period
{
    private int periodID;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    public Period(){
        start = LocalDate.now();
        end = LocalDate.now();
    }

    public Period(LocalDate startDate, LocalDate endDate)
    {
        this.start = startDate;
        this.end = endDate;
    }

    public int getPeriodID()
    {
        return periodID;
    }

    public void setPeriodID(int periodID)
    {
        this.periodID = periodID;
    }

    /*
     constructor avoided for integration with Spring/Thymeleaf (which requires
     exactly one parameterless public and one fully parameterized constructor)
    */
    /** Method returns an initialized Period object */
    public static Period periodfromDates(java.sql.Date startDate, java.sql.Date endDate)
    {
        return new Period(startDate.toLocalDate(),
                          endDate.toLocalDate());
    }

    /**
     * Method returns true if this period overlaps with
     */
    public boolean overlapsWith(Period otherPeriod)
    {
        return DateUtil.doPeriodsOverlap(
                start, end, otherPeriod.getStart(), otherPeriod.getEnd());
    }

    /**
     * Method returns false if periods overlap or the other start date is earlier than the start date
     * of this period; so you can !/not the return value and be sure the other period is earlier than this
     * period.
     */
    public boolean isEarlierThan(Period otherPeriod)
    {
        /* if periods overlap, this implies that neither period i entirely ealier than the other */
        if(overlapsWith(otherPeriod)){ return false; }
        return start.isBefore(otherPeriod.getStart());

        // could also be handled in a single line:
        // return end.isBefore(otherPeriod.getStart());
    }



    /**
     * Method returns true if supplied date is before start or after end of period.
     */
    public boolean dateOverlapsWithPeriod(LocalDate date)
    {
        return !(date.isBefore(start) || date.isAfter(end));
    }
    /**
     * Return an int[3], where index 0 = day, 1 = months, 2 = years */
    public int[] duration(){

        int[] result = new int[3];

        java.time.Period javaPeriod = start.until(end);

        result[0] = javaPeriod.getDays();
        result[1] = javaPeriod.getMonths();
        result[2] = javaPeriod.getYears();

        return result;
    }



    public boolean isPeriodConsistent()
    {
        return DateUtil.isPeriodConsistent(start, end);
    }

    public LocalDate getStart(){ return start; }

    public void setStart(LocalDate start){ this.start = start; }

    public Date getStartAsDate(){return DateUtil.convertToDate(start);}

    public LocalDate getEnd(){ return end; }

    public Date getEndAsDate(){return DateUtil.convertToDate(end);}

    public void setEnd(LocalDate end){ this.end = end; }


    @Override
    public String toString()
    {
        return "Period{" +
               "start=" + start +
               ", end=" + end +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof Period)) return false;
        Period period = (Period)o;
        return getStart().equals(period.getStart()) &&
               getEnd().equals(period.getEnd());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getStart(), getEnd());
    }
}
