package calendarControllerTests.calendarTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.EditEventCommand;

/**
 * A JUnit test that tests for the controller being able to edit calendars.
 */
public class CalendarControllerEditCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testEditCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateEventCommand createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    String editCalendar = " calendar --name test --property name \"test_edit\"";
    EditEventCommand editEventCommand = new EditEventCommand(editCalendar,
            calendarManagement, calendarView);

    editEventCommand.execute();
  }
}
