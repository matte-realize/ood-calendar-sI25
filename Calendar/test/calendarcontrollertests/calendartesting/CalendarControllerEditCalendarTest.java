package calendarcontrollertests.calendartesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Scanner;

import controller.CreateCommand;
import controller.EditCommand;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to edit calendars.
 */
public class CalendarControllerEditCalendarTest extends AbstractControllerCalendarTest {

  @Test
  public void testEditCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateCommand createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);
    createCommand.execute();

    calendarManagement.selectCalendar("test");

    assertEquals(ZoneId.of("Europe/Paris"), calendarManagement.getCalendarTimezone("test"));

    String editCalendarName = " calendar --name test --property name testEdit";
    EditCommand editCommand1 = new EditCommand(editCalendarName,
            calendarManagement, calendarView);
    editCommand1.execute();

    assertEquals(ZoneId.of("Europe/Paris"), calendarManagement.getCalendarTimezone("testEdit"));

    String editCalendarTimeZone = " calendar --name testEdit --property timezone Africa/Harare";
    EditCommand editCommand2 = new EditCommand(editCalendarTimeZone,
            calendarManagement, calendarView);
    editCommand2.execute();

    assertEquals(ZoneId.of("Africa/Harare"), calendarManagement.getCalendarTimezone("testEdit"));
  }

  @Test
  public void testInvalidEditOnCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateCommand createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);
    createCommand.execute();

    calendarManagement.selectCalendar("test");

    String editCalendar = " calendar --name test --property timezone \"test_edit\"";
    EditCommand editCommand = new EditCommand(editCalendar,
            calendarManagement, calendarView);
    editCommand.execute();

    String simulatedInput = "Invalid new value format. Make sure the new value "
            + "is of the same type as you are trying to edit\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String firstLine = scanner.nextLine();
    scanner.close();

    assertEquals("Invalid new value format. Make sure the new value "
            + "is of the same type as you are trying to edit", firstLine);
  }
}
