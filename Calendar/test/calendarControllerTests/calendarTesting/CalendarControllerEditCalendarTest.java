package calendarControllerTests.calendarTesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    String editCalendar = " calendar --name test --property name \"test_edit\"";
    EditCommand editCommand = new EditCommand(editCalendar,
            calendarManagement, calendarView);
    editCommand.execute();
  }

  @Test
  public void testInvalidEditOnCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateCommand createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);
    createCommand.execute();

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
