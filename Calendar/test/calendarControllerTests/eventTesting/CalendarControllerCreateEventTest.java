package calendarControllerTests.eventTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test that tests for the controller being able to create events.
 */
public class CalendarControllerCreateEventTest extends AbstractControllerEventTest {
  @Test
  public void testCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testCreateEventSeriesByRange() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testCreateEventSeriesByInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-06-10 repeats T for 4 times";
    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testInvalidCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    assertEquals("Timezone is of invalid format", calendarView.lastError);
  }

  @Test
  public void testInvalidCreateEventSeries() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateEventCommand createCommand1 = new CreateEventCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand1.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand1.execute();
    });

    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-08-12 repeats T for 4 times";
    CreateEventCommand createCommand2 = new CreateEventCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand2.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand2.execute();
    });
  }
}
