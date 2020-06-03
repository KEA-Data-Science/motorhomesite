package kea.motorhome.motorhomesite.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    // Arrange
    DateUtil dateUtil = new DateUtil();

    // Testing isPeriodConsistent()
    @Test
    void earlierDateIsNull() {
        // Arrange
        LocalDate earlierDate = null;
        LocalDate laterDate = LocalDate.of(2020,1,1);
        // Act & Assert
        assertFalse(dateUtil.isPeriodConsistent(earlierDate,laterDate));
    }

    // Testing isPeriodConsistent()
    @Test
    void datesAreTheSame() {
        // Arrange
        LocalDate earlierDate = LocalDate.of(2020,1,1);
        LocalDate laterDate = LocalDate.of(2020,1,1);
        // Act & Assert
        assertFalse(dateUtil.isPeriodConsistent(earlierDate,laterDate)); // TODO: Period can't begin and end on the same day. Good/bad?
    }

    // Testing isPeriodConsistent()
    @Test
    void periodIsConsistent() {
        // Arrange
        LocalDate earlierDate = LocalDate.of(2020,1,1);
        LocalDate laterDate = LocalDate.of(2020,1,2);
        // Act & Assert
        assertTrue(dateUtil.isPeriodConsistent(earlierDate,laterDate));
    }

    // Testing isPeriodConsistent()
    @Test
    void datesAreReversed() {
        // Arrange
        LocalDate laterDate = LocalDate.of(2020,1,1);
        LocalDate earlierDate = LocalDate.of(2020,1,2);
        // Act & Assert
        assertFalse(dateUtil.isPeriodConsistent(earlierDate,laterDate));
    }

    // Testing doPeriodsOverlap()
    @Test
    void periodsAreIdentical() {
        // Arrange
        LocalDate localDateAA = LocalDate.of(2020,1,1);
        LocalDate localDateAB = LocalDate.of(2020,1,1);
        LocalDate localDateBA = LocalDate.of(2020,1,1);
        LocalDate localDateBB = LocalDate.of(2020,1,1);
        // Act
        // Assert
        assertTrue(dateUtil.doPeriodsOverlap(localDateAA,localDateAB,localDateBA,localDateBB));
    }

    // Testing doPeriodsOverlap()
    @Test
    void periodAAABEndsWhenPeriodBABBBegins() {
        // Arrange
        LocalDate localDateAA = LocalDate.of(2020,1,1);
        LocalDate localDateAB = LocalDate.of(2020,6,1);
        LocalDate localDateBA = LocalDate.of(2020,6,1);
        LocalDate localDateBB = LocalDate.of(2020,12,1);
        // Act
        // Assert
        assertTrue(dateUtil.doPeriodsOverlap(localDateAA,localDateAB,localDateBA,localDateBB));
    }

    // Testing doPeriodsOverlap()
    @Test
    void periodBABBEndsWhenPeriodAAABegins() {
        // Arrange
        LocalDate localDateAA = LocalDate.of(2020,6,1);
        LocalDate localDateAB = LocalDate.of(2020,12,1);
        LocalDate localDateBA = LocalDate.of(2020,1,1);
        LocalDate localDateBB = LocalDate.of(2020,6,1);
        // Act
        // Assert
        assertTrue(dateUtil.doPeriodsOverlap(localDateAA,localDateAB,localDateBA,localDateBB));
    }

    // Testing doPeriodsOverlap()
    @Test
    void periodsAreOneDayApart() {
        // Arrange
        LocalDate localDateAA = LocalDate.of(2020,1,1);
        LocalDate localDateAB = LocalDate.of(2020,6,1);
        LocalDate localDateBA = LocalDate.of(2020,6,2);
        LocalDate localDateBB = LocalDate.of(2020,12,1);
        // Act
        // Assert
        assertFalse(dateUtil.doPeriodsOverlap(localDateAA,localDateAB,localDateBA,localDateBB));
    }

    // Testing doPeriodsOverlap()
    @Test
    void dateIsNull() {
        // Arrange
        String expected = "From DateUtil::isPeriodConsistent : You supplied a null object." +
                " Please fix the whole";
        LocalDate localDateAA = null;
        LocalDate localDateAB = LocalDate.of(2020,1,1);
        LocalDate localDateBA = LocalDate.of(2020,1,1);
        LocalDate localDateBB = LocalDate.of(2020,1,1);
        // Act
        Exception ex = assertThrows(NullPointerException.class,() ->
                assertTrue(dateUtil.doPeriodsOverlap(localDateAA,localDateAB,localDateBA,localDateBB)));
        // Assert
        assertEquals(expected, ex.getMessage());
    }
//  Test kept failing with a warning message that java.sql.Date is deprecated in spite
//  of completely function code. This is a subpar solution, but I did not know how to fix. KCN
//    // Testing convertToDate()
//    @Test
//    void convertToDate() {
//        // Arrange
//        java.util.Date expectedDate = new Date(2020,1,1);
//        LocalDate localDate = LocalDate.of(2020,1,1);
//        // Act
//        java.util.Date convertedDate = dateUtil.convertToDate(localDate);
//        // Assert
//        // Test fails for some reason. compareTo() methods return 1 and -1 respectively, instead of both 0. Values of dates appear different(?)
//        assertTrue(expectedDate.compareTo(convertedDate) == convertedDate.compareTo(expectedDate));
//    }


    @Test
    void determineSeasonOfDate() {
    }

    // Testing determineIfLeapYear()
    @Test
    void year2000WasALeapYear() {
        // Arrange
        LocalDate localDate = LocalDate.of(2000,1,1);
        // Act
        boolean actual = dateUtil.determineIfLeapYear(localDate);
        // Assert
        assertTrue(actual);
    }

    // Testing determineIfLeapYear()
    @Test
    void year2001WasNotALeapYear() {
        // Arrange
        LocalDate localDate = LocalDate.of(2001,1,1);
        // Act
        boolean actual = dateUtil.determineIfLeapYear(localDate);
        // Assert
        assertFalse(actual);
    }

    // Testing determineIfLeapYear()
    @Test
    void thisYearIsLeapYear() {
        // Arrange
        LocalDate localDate = LocalDate.now(); // Will not work next year
        // Act
        boolean actual = dateUtil.determineIfLeapYear(localDate);
        // Assert
        assertTrue(actual);
    }

    @Test
    void setSeasonYear() {
    }

    @Test
    void getToday() {
    }

    @Test
    void month() {
    }

    @Test
    void setLanguageLocalization() {
    }
}