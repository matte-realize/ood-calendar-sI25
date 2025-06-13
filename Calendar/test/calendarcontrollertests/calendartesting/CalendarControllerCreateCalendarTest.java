package calendarcontrollertests.calendartesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Scanner;

import controller.CreateCommand;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to create calendars.
 */
public class CalendarControllerCreateCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testCreateCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateCommand createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    // verifies the existence of the calendar
    assertEquals(ZoneId.of("Europe/Paris"), calendarManagement.getCalendarTimezone("test"));
  }

  @Test
  public void testCreateCalendarSameTimeZoneDifferentName() {
    String createCalendar1 = " calendar --name test --timezone Europe/Paris";
    String createCalendar2 = " calendar --name test2 --timezone Europe/Paris";
    CreateCommand createCommand1 = new CreateCommand(createCalendar1,
            calendarManagement, calendarView);
    CreateCommand createCommand2 = new CreateCommand(createCalendar2,
            calendarManagement, calendarView);

    createCommand1.execute();
    createCommand2.execute();

    assertEquals(ZoneId.of("Europe/Paris"), calendarManagement.getCalendarTimezone("test"));
    assertEquals(ZoneId.of("Europe/Paris"), calendarManagement.getCalendarTimezone("test2"));
  }

  @Test
  public void testInvalidNameCreateCalendar() {
    String invalidTimeZone = " calendar --name invalid --timezone America/Shanghai";

    CreateCommand createCommand1 = new CreateCommand(invalidTimeZone,
            calendarManagement, calendarView);

    String simulatedInput = "Timezone is of invalid format\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);

    createCommand1.execute();

    Scanner scanner = new Scanner(System.in);
    String firstLine = scanner.nextLine();
    scanner.close();

    assertEquals("Timezone is of invalid format", firstLine);

    System.setIn(System.in);
  }

  @Test
  public void testInvalidTimeZoneCreateCalendar() {
    String invalidTimeZone = " calendar --name invalid --timezone America/Shanghai";

    CreateCommand createCommand1 = new CreateCommand(invalidTimeZone,
            calendarManagement, calendarView);

    String simulatedInput = "Timezone is of invalid format\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);

    createCommand1.execute();

    Scanner scanner = new Scanner(System.in);
    String firstLine = scanner.nextLine();
    scanner.close();

    assertEquals("Timezone is of invalid format", firstLine);

    System.setIn(System.in);
  }
}
