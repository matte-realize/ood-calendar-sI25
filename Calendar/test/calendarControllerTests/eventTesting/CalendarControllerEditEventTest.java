package calendarControllerTests.eventTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.EditEventCommand;

/**
 * A JUnit test that tests for the controller being able to edit events.
 */
public class CalendarControllerEditEventTest extends AbstractControllerEventTest {
  @Test
  public void testEditSingleEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEvent = " event subject \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00 with Edited Event One";
    EditEventCommand editCommand = new EditEventCommand(editEvent, calendarManagement, calendarView);
    editCommand.execute();
  }
}
