package calendarControllerTests;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;


public class CalendarControllerCreateEventTest extends AbstractControllerTest {
  @Test
  public void testCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent, calendarManagement);

    createCommand.execute();
  }
}
