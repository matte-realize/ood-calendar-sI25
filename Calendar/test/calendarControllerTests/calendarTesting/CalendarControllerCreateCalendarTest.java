package calendarControllerTests.calendarTesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import controller.eventCommands.CreateEventCommand;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to create calendars.
 */
public class CalendarControllerCreateCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testCreateCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateEventCommand createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();
  }

  @Test
  public void testInvalidNameCreateCalendar() {
    String invalidTimeZone = " calendar --name invalid --timezone America/Shanghai";

    CreateEventCommand createCommand1 = new CreateEventCommand(invalidTimeZone,
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

    CreateEventCommand createCommand1 = new CreateEventCommand(invalidTimeZone,
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
