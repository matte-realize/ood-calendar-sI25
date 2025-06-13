package calendarmodeltests.calendarmodelcalendartests;

import org.junit.Test;

import java.time.ZoneId;

import model.calendar.CalendarModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * A JUnit test class that tests the model for creating a calendar.
 */
public class CalendarCreateCalenderTest {
  @Test
  public void testCreateCalendar() {
    String calendarName = "Work Calendar";
    ZoneId timeZone = ZoneId.of("America/New_York");

    CalendarModel.CustomCalendarBuilder builder = new CalendarModel.CustomCalendarBuilder();

    CalendarModel calendar = builder
         .setName(calendarName)
         .setTimeZone(timeZone)
         .build();

    assertEquals(calendarName, calendar.getName());
    assertEquals(timeZone, calendar.getTimeZone());
  }

  @Test
  public void testInvalidCreateCalendar() {
    ZoneId validTimeZone = ZoneId.of("America/New_York");

    try {
      CalendarModel calendar = new CalendarModel(null, validTimeZone);
      assertNull("Null name should be preserved", calendar.getName());
      assertEquals("Timezone should still be set", validTimeZone, calendar.getTimeZone());
    } catch (Exception e) {
      // catches exception
    }

    String validName = "Test Calendar";

    try {
      CalendarModel calendar = new CalendarModel(validName, null);
      assertEquals("Name should be set", validName, calendar.getName());
      assertNull("Null timezone should be preserved", calendar.getTimeZone());
    } catch (Exception e) {
      fail("Should handle null timezone gracefully or document expected exception");
    }

    try {
      CalendarModel calendar = new CalendarModel(null, null);
      assertNull("Both values should be null", calendar.getName());
      assertNull("Both values should be null", calendar.getTimeZone());
    } catch (Exception e) {
      // catches exception
    }

    String validName4 = "Test Calendar";

    try {
      ZoneId invalidZone = ZoneId.of("Invalid/Timezone");
      CalendarModel calendar = new CalendarModel(validName4, invalidZone);
      fail("Should not reach here with invalid timezone");
    } catch (Exception e) {
      // catches exception
    }
  }
}
