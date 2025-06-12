package calendarControllerTests.calendarTesting;

import org.junit.Test;

/**
 * A JUnit test that tests for the controller being able to create calendars.
 */
public class CalendarControllerCreateCalendarTest {
  @Test
  public void testCreateCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";

  }

  @Test
  public void testInvalidCreateCalendar() {
    String invalidCreate = " calendar --name invalid --timezone America/Shanghai";
  }
}
