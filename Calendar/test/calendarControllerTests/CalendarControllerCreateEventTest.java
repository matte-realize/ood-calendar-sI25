package calendarControllerTests;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;

public class CalendarControllerCreateEventTest {
  @Test
  public void testCreateEvent() {
    String command = "create event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(command, );

    createCommand.execute();
  }
}
