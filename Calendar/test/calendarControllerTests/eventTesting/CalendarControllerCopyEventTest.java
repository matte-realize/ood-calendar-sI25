package calendarControllerTests.eventTesting;

import org.junit.Before;
import org.junit.Test;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.EditEventCommand;

/**
 * A JUnit test that tests for the controller being able to copy events.
 */
public class CalendarControllerCopyEventTest extends AbstractControllerEventTest {

  @Before
  public void setUp() {
    String createCalendar = " calendar --name newCalendar --timezone Europe/Paris";
    CreateEventCommand createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    createCalendar = " calendar --name emptyCalendar --timezone Europe/Paris";
    createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();
  }

  @Test
  public void testCopyEvents() {
    String createCalendar = " calendar --name newCalendar --timezone Europe/Paris";
    CreateEventCommand createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    createCalendar = " calendar --name emptyCalendar --timezone Europe/Paris";
    createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();
  }
}
