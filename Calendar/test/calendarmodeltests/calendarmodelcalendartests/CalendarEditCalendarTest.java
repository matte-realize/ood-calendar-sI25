package calendarmodeltests.calendarmodelcalendartests;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import model.calendar.CalendarManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test for testing editing calendars.
 */
public class CalendarEditCalendarTest {
  private CalendarManagement calendarManagement;

  @Before
  public void setup() {
    calendarManagement = new CalendarManagement();
  }

  @Test
  public void testEditCalendarName() {
    String calendarName = "Work Calendar";
    ZoneId timeZone = ZoneId.of("America/New_York");

    calendarManagement.createCalendar(calendarName, timeZone);
    calendarManagement.selectCalendar(calendarName); // verifies the name exists

    String editName = "NY";

    calendarManagement.editCalendar(calendarName, "name", editName);
    calendarManagement.selectCalendar(editName); // verifies the name exists

    assertThrows(IllegalArgumentException.class, () -> {
      calendarManagement.createCalendar(editName, ZoneId.of("America/New_York"));
    }); // can not create a calendar with the same name
  }

  @Test
  public void testEditCalendarTimeZone() {
    String calendarName = "Work Calendar";
    ZoneId timeZone = ZoneId.of("America/New_York");

    calendarManagement.createCalendar(calendarName, timeZone);

    assertEquals(timeZone, calendarManagement.getCalendarTimezone("Work Calendar"));

    calendarManagement.editCalendar(calendarName, "timezone", "Asia/Tokyo");

    assertEquals("Asia/Tokyo", calendarManagement.getCalendarTimezone(calendarName).toString());
  }

  @Test
  public void testEditCalendarNameToExistingName() {
    String firstName = "First Calendar";
    String secondName = "Second Calendar";
    ZoneId timeZone = ZoneId.of("America/New_York");

    calendarManagement.createCalendar(firstName, timeZone);
    calendarManagement.createCalendar(secondName, timeZone);

    assertThrows(IllegalArgumentException.class, () -> {
      calendarManagement.editCalendar(firstName, "name", secondName);
    });
  }

  @Test
  public void testEditNonExistentCalendar() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendarManagement.editCalendar("NonExistent", "name", "NewName");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendarManagement.editCalendar("NonExistent", "timezone", "America/Chicago");
    });
  }

  @Test
  public void testEditCalendarWithInvalidProperty() {
    String calendarName = "Test Calendar";
    ZoneId timeZone = ZoneId.of("America/New_York");

    calendarManagement.createCalendar(calendarName, timeZone);

    assertThrows(IllegalArgumentException.class, () -> {
      calendarManagement.editCalendar(calendarName, "invalidProperty", "someValue");
    });
  }
}
